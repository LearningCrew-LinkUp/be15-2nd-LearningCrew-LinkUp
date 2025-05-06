package com.learningcrew.linkupuser.command.application.service;

import com.learningcrew.linkupuser.command.application.dto.request.FindPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.request.LoginRequest;
import com.learningcrew.linkupuser.command.application.dto.request.ResetPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.response.TokenResponse;
import com.learningcrew.linkupuser.command.domain.aggregate.Member;
import com.learningcrew.linkupuser.command.domain.aggregate.RefreshToken;
import com.learningcrew.linkupuser.command.domain.aggregate.User;
import com.learningcrew.linkupuser.command.domain.aggregate.VerificationToken;
import com.learningcrew.linkupuser.command.domain.constants.EmailTokenType;
import com.learningcrew.linkupuser.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkupuser.command.domain.repository.MemberRepository;
import com.learningcrew.linkupuser.command.domain.repository.RefreshtokenRepository;
import com.learningcrew.linkupuser.command.domain.repository.UserRepository;
import com.learningcrew.linkupuser.command.domain.repository.VerificationTokenRepository;
import com.learningcrew.linkupuser.command.domain.service.TokenDomainService;
import com.learningcrew.linkupuser.command.domain.service.UserDomainServiceImpl;
import com.learningcrew.linkupuser.command.domain.service.UserValidatorServiceImpl;
import com.learningcrew.linkupuser.common.util.DefaultImageProperties;
import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import com.learningcrew.linkupuser.exception.security.CustomJwtException;
import com.learningcrew.linkupuser.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshtokenRepository refreshtokenRepository;
    private final UserValidatorServiceImpl userValidatorService;
    private final TokenDomainService tokenDomainService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserDomainServiceImpl userDomainService;
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final DefaultImageProperties defaultImageProperties;

    /* 로그인 기능 */
    @Transactional
    public TokenResponse login(LoginRequest request) {

        // 이메일로 회원 조회
        User user = userValidatorService.validateEmail(request.getEmail());

        // 활성화 상태 확인
        userValidatorService.validateUserStatus(user.getStatus().getStatusType(), LinkerStatusType.ACCEPTED.name());

        // 삭제 여부 확인
        userValidatorService.isDeletedUser(user.getDeletedAt());

        // 비밀번호 확인
        userValidatorService.validatePassword(request.getPassword(), user.getPassword());

        // 토큰 발급
        String accessToken = tokenDomainService.generateToken(user);
        String refreshToken = tokenDomainService.generateRefreshToken(user);

        //refresh token 저장
        tokenDomainService.saveRefreshToken(user.getUserId(),user.getEmail(), refreshToken);

        // member 테이블 조회
        String profileImageUrl = memberRepository.findById(user.getUserId())
                .map(Member::getProfileImageUrl)      // 있으면 해당 프로필
                .orElse(defaultImageProperties.getDefaultProfileImage());      // 없으면 기본 프로필

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userName(user.getUserName())
                .profileImageUrl(profileImageUrl)
                .build();
    }

    /* 토큰 재발급  */
    @Transactional
    public TokenResponse refreshToken(String providedRefreshToken) {
        jwtTokenProvider.validateToken(providedRefreshToken);
        String userId = jwtTokenProvider.getUserIdFromJWt(providedRefreshToken);

        RefreshToken storedToken = refreshtokenRepository.findById(Integer.parseInt(userId)).orElseThrow(()->new CustomJwtException(ErrorCode.USER_NOT_FOUND));

        if(!storedToken.getToken().equals(providedRefreshToken)) {
            throw new CustomJwtException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        if(storedToken.getExpiryDate().before(new Date())) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT);
        }

        User user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        //토큰 발급
        String accessToken = tokenDomainService.generateToken(user);
        String refreshToken = tokenDomainService.generateRefreshToken(user);

        //refresh token 저장
        tokenDomainService.saveRefreshToken(user.getUserId(), user.getEmail(), refreshToken);

        // member 테이블 조회
        String profileImageUrl = memberRepository.findById(user.getUserId())
                .map(Member::getProfileImageUrl)      // 있으면 해당 프로필
                .orElse(defaultImageProperties.getDefaultProfileImage());      // 없으면 기본 프로필
        
        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userName(user.getUserName())
                .profileImageUrl(profileImageUrl)
                .build();
    }

    /* 로그아웃 */
    @Transactional
    public void logout(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUserIdFromJWt(refreshToken);
        refreshtokenRepository.deleteById(Integer.parseInt(userId));
    }

    /* 이메일 인증 */
    @Transactional
    public void verifyEmail(String tokenCode) {
        // 토큰 유효성 검사
        VerificationToken verificationToken = verificationTokenRepository.findByCode(tokenCode).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_VERIFICATION_TOKEN)
        );

        // 토큰 만료시간 검사
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.EXPIRE_VERIFICATION_CODE);
        }

        // 토큰 타입 검사
        if (!verificationToken.getTokenType().equals(EmailTokenType.REGISTER.name())) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        }

        // 유저 조회
        User user = userRepository.findById(verificationToken.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 상태 확인
        userValidatorService.validateUserStatus(user.getStatus().getStatusType(),LinkerStatusType.PENDING.name());

        // 삭제 여부 확인
        userValidatorService.isDeletedUser(user.getDeletedAt());

        // 상태 활성화
        userDomainService.activateUser(user);

        /* 토큰 삭제 */
        verificationTokenRepository.delete(verificationToken);
    }

    /* 비밀번호 재설정 링크 전송 */
    @Override
    @Transactional
    public void sendPasswordResetLink(FindPasswordRequest request) {
        // 유저 조회
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        // 활성화 상태 확인
        userValidatorService.validateUserStatus(user.getStatus().getStatusType(),LinkerStatusType.ACCEPTED.name());

        // 삭제 여부 확인
        userValidatorService.isDeletedUser(user.getDeletedAt());


        // 이메일 전송
        emailService.sendVerificationCode(user.getUserId(), user.getEmail(), user.getUserName(), EmailTokenType.RESET_PASSWORD.name());
    }


    /* 비밀번호 재설정 */
    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // 토큰 유효성 검사
        VerificationToken verificationToken = verificationTokenRepository.findByCode(request.getToken()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_VERIFICATION_TOKEN)
        );

        // 토큰 만료시간 검사
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.EXPIRE_VERIFICATION_CODE);
        }

        // 토큰 타입 검사
        if (!verificationToken.getTokenType().equals(EmailTokenType.RESET_PASSWORD.name())) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        }

        // 유저 조회
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );


        // 토큰 내 이메일이 있는지 확인
        if (!verificationToken.getEmail().equals(user.getEmail())) {
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_TOKEN);
        }


        // 활성화 상태 확인
        userValidatorService.validateUserStatus(user.getStatus().getStatusType(),LinkerStatusType.ACCEPTED.name());

        // 삭제 여부 확인
        userValidatorService.isDeletedUser(user.getDeletedAt());

        // 이전 비밀번호 중복 검사

        userValidatorService.validateDuplicatePassword(request.getNewPassword(), user.getPassword());

        // 비밀번호 암호화
        user.setPassword(request.getNewPassword());

        userValidatorService.validateDuplicatePassword(user.getPassword(), request.getNewPassword());

        // 비밀번호 암호화

        userDomainService.encryptPassword(user);

        userRepository.save(user);

        /* 토큰 삭제 */
        verificationTokenRepository.delete(verificationToken);
    }
}
