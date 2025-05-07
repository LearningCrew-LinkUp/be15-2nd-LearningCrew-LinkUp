package com.learningcrew.linkupuser.command.domain.service;


import com.learningcrew.linkupuser.command.domain.aggregate.User;
import com.learningcrew.linkupuser.command.domain.constants.LinkerStatusType;
import com.learningcrew.linkupuser.command.domain.repository.MemberRepository;
import com.learningcrew.linkupuser.command.domain.repository.UserRepository;
import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserValidatorServiceImpl {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /* 이메일 중복 검사*/
    public void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    /* 전화번호 중복 검사 */
    public void validateDuplicatePhone(String phoneNumber) {
        if (userRepository.existsByContactNumber(phoneNumber)) {
            throw new BusinessException(ErrorCode.DUPLICATE_CONTACT_NUMBER);
        }
    }

    /* 닉네임 중복 검사 */
    public void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    /* 비밀번호 유효성 검사 */
    public void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    /* 비밀번호 중복 검사 */

    public void validateDuplicatePassword(String rawPassword, String encodedPassword) {
        if (passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PASSWORD);
        }
    }

    /* 이메일 인증 검사 */
    public User validateEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.INVALID_CREDENTIALS)
                );
    }

    public void isWithinRecoveryPeriod(String statusType, LocalDateTime deletedAt) {
        // 삭제된 회원인지 확인
        if (Objects.isNull(deletedAt) || !statusType.equals(LinkerStatusType.DELETED.name())) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_RECOVERABLE);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = deletedAt.plusDays(90);

        // 복구 유효기간 내 회원인지 확인
        if(!now.isBefore(expiry)){
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_RECOVERABLE);
        }
    }

    public void validateUserStatus(String providedStatus, String targetStatus) {
        if(!(providedStatus.equals(targetStatus))) {
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }
    }

    public void isDeletedUser(LocalDateTime userDeletedAt){
        if(Objects.nonNull(userDeletedAt)){
            throw new BusinessException(ErrorCode.WITHDRAW_USER);
        }
    }
}

