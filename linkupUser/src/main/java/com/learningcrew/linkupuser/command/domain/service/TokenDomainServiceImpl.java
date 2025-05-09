package com.learningcrew.linkupuser.command.domain.service;


import com.learningcrew.linkupuser.command.domain.aggregate.RefreshToken;
import com.learningcrew.linkupuser.command.domain.aggregate.User;
import com.learningcrew.linkupuser.command.domain.aggregate.VerificationToken;
import com.learningcrew.linkupuser.command.domain.repository.RefreshtokenRepository;
import com.learningcrew.linkupuser.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenDomainServiceImpl implements TokenDomainService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshtokenRepository refreshtokenRepository;

    public String generateToken(User user) {
        return jwtTokenProvider.createToken(user.getUserId(), user.getRole().getRoleName());
    }

    public String generateRefreshToken(User user) {
        System.out.println("refresh token을 생성합니다");
        return jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRole().getRoleName());
    }

    public void saveRefreshToken(int userId, String email, String refreshToken) {
        System.out.println("refresh token을 생성합니다");
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(userId)
                .userEmail(email)
                .token(refreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshTokenExpiration()))
                .build();

        refreshtokenRepository.save(refreshTokenEntity);
    }

    public void delete(VerificationToken verificationToken) {
    }
}
