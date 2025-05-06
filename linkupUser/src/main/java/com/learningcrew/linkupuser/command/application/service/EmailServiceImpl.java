package com.learningcrew.linkupuser.command.application.service;


import com.learningcrew.linkupuser.exception.BusinessException;
import com.learningcrew.linkupuser.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final VerificationTokenServiceImpl verificationTokenService;

    private static final int codeValidTime = 10; // 인증 코드 유효 시간 (10분)

    /* 회원가입 인증코드 전송 */
    public void sendVerificationCode(int userId, String email, String userName, String tokenType) {
        log.info("========인증코드 발송 서비스 - sendSignUpEmail : email: {}, userName: {}, tokenType: {} ========", email,userName,tokenType );

        String subject;
        String templateName;

        switch (tokenType) {
            case "REGISTER" -> {
                subject = "[Linker] 회원가입 인증코드입니다.";
                templateName = "email/verification-code";
            }
            case "RESET_PASSWORD" -> {
                subject = "[Linker] 비밀번호 재설정 링크입니다.";
                templateName = "email/password-reset"; // 필요시 새 템플릿
            }
            case "ACCOUNT_RECOVERY" -> {
                subject = "[Linker] 계정 복구 인증코드입니다.";
                templateName = "email/recovery-code";
            }
            default -> throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE);
        }
        // 인증 코드 저장
        String verificationCode = verificationTokenService.createAndSaveToken(userId, email, tokenType);

        // 인증 코드 만료 시간 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expireMinute = LocalDateTime.now().plusMinutes(codeValidTime).format(formatter);

        // 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("verificationCode", verificationCode);
            context.setVariable("email", email);
            context.setVariable("expireTime", expireMinute + "분");

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        }catch (MessagingException e){
            throw new BusinessException(ErrorCode.FAIL_TO_SEND_EMAIL);
        }

    }


}
