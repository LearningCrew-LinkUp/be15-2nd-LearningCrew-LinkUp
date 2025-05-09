package com.learningcrew.linkupuser.command.application.service;


import com.learningcrew.linkupuser.command.application.dto.ProfileUpdateRequest;
import com.learningcrew.linkupuser.command.application.dto.request.UserCreateRequest;
import com.learningcrew.linkupuser.command.application.dto.response.RegisterResponse;
import com.learningcrew.linkupuser.command.domain.aggregate.Member;
import com.learningcrew.linkupuser.command.domain.aggregate.User;
import com.learningcrew.linkupuser.command.domain.constants.EmailTokenType;
import com.learningcrew.linkupuser.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkupuser.command.domain.repository.MemberRepository;
import com.learningcrew.linkupuser.command.domain.repository.UserRepository;
import com.learningcrew.linkupuser.command.domain.service.MemberDomainServiceImpl;
import com.learningcrew.linkupuser.command.domain.service.UserDomainServiceImpl;
import com.learningcrew.linkupuser.command.domain.service.UserValidatorServiceImpl;
import com.learningcrew.linkupuser.common.domain.Status;
import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountCommandServiceImpl implements AccountCommandService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserValidatorServiceImpl userValidatorService;
    private final UserDomainServiceImpl userDomainService;
    private final MemberDomainServiceImpl memberDomainService;
    private final EmailService emailService;

    private final MemberRepository memberRepository;

    /* 회원 가입 */
    @Transactional
    public RegisterResponse registerUser(@Valid UserCreateRequest request) {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            Status currentStatus = existingUser.get().getStatus();
            if (currentStatus.getStatusType().equals(LinkerStatusType.ACCEPTED.name())) {
                throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
            }

            if (currentStatus.getStatusType().equals(LinkerStatusType.PENDING.name())) {
                throw new BusinessException(ErrorCode.SIGNUP_PENDING);
            }
        }


        User user = modelMapper.map(request, User.class);


        // 중복 체크
        userValidatorService.validateDuplicateEmail(request.getEmail());
        userValidatorService.validateDuplicatePhone(request.getContactNumber());
        userValidatorService.validateDuplicateNickname(request.getNickname());

        // 비밀번호 암호화
        userDomainService.encryptPassword(user);

        // 권한 부여
        userDomainService.assignRole(user, "USER");

        //상태 부여
        userDomainService.assignStatus(user, "PENDING");

        // User 엔티티 생성 및 저장
        userRepository.save(user);

        Member member = modelMapper.map(request, Member.class);

        // 기본 프로필 이미지 저장
        memberDomainService.setDefaultProfileImage(member);

        // 닉네임 자동 생성
        memberDomainService.assignNicknameIfInvalid(member);

        // Member 엔티티 생성 및 저장
        memberDomainService.saveMember(member, user.getUserId());

        // 이메일 전송 및 저장
        emailService.sendVerificationCode(user.getUserId(), user.getEmail(), user.getUserName(), EmailTokenType.REGISTER.name());

        return RegisterResponse
                .builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .status("PENDING")
                .build();
    }

    /* 회원 탈퇴 */
    @Transactional
    @Override
    public void withdrawUser(String requestPassword, int userId) {
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        // 활성화 상태 확인
        userValidatorService.validateUserStatus(user.getStatus().getStatusType(), LinkerStatusType.ACCEPTED.name());

        // 삭제 여부 확인
        userValidatorService.isDeletedUser(user.getDeletedAt());

        //비밀번호 검증
        userValidatorService.validatePassword(requestPassword, user.getPassword());

        //상태를 deleted로 변경
        userDomainService.assignStatus(user, LinkerStatusType.DELETED.name());

        //회원 탈퇴 시간 삽입
        user.setDeletedAt();

        //저장
        userRepository.save(user);
    }

    /* 회원 계정 복구 */
    @Override
    @Transactional
    public void recoverUser(String email, String password) {
        // 회원 조회
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        // 비밀번호 검사
        userValidatorService.validatePassword(password, user.getPassword());

        // 계정 복구 유효기간(90일) 넘었는지 확인
        userValidatorService.isWithinRecoveryPeriod(user.getStatus().getStatusType(), user.getDeletedAt());

        // 상태 활성화
        userDomainService.assignStatus(user,LinkerStatusType.ACCEPTED.name());

        // 삭제일시 초기화
        user.setDeletedAt(null);

        userRepository.save(user);
    }


    /* 프로필 수정 */
    @Override
    @Transactional
    public void updateProfile(int userId, ProfileUpdateRequest request) {
        // 회원 조회
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        // 활성화 상태 확인
        userValidatorService.validateUserStatus(user.getStatus().getStatusType(),LinkerStatusType.ACCEPTED.name());

        // 삭제 여부 확인
        userValidatorService.isDeletedUser(user.getDeletedAt());

        // 프로필 조회
        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        // 프로필 수정
        member.updateProfile(request);

        userRepository.save(user);
    }

    /* 포인트 증가 */
    @Override
    @Transactional
    public void increaseUserPoint(int userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        int current = user.getPointBalance();
        user.setPointBalance(current + amount);
        userRepository.save(user);
//        userRepository.save(user); 트랜잭션이 커밋될 때 자동으로 DB에 반영
    }

    /* 포인트 감소 */
    @Override
    @Transactional
    public void decreaseUserPoint(int userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getPointBalance() < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
        }

        user.setPointBalance(user.getPointBalance() - amount);
//        userRepository.save(user); 트랜잭션이 커밋될 때 자동으로 DB에 반영
    }
}
