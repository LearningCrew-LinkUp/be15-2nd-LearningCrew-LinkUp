package com.learningcrew.linkupuser.command.application.service;


import com.learningcrew.linkupuser.command.application.dto.request.FindPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.request.LoginRequest;
import com.learningcrew.linkupuser.command.application.dto.request.RefreshTokenRequest;
import com.learningcrew.linkupuser.command.application.dto.request.ResetPasswordRequest;
import com.learningcrew.linkupuser.command.application.dto.response.TokenResponse;
import jakarta.validation.Valid;

public interface AuthCommandService {

    TokenResponse login(LoginRequest request);

    TokenResponse refreshToken(String providedRefreshToken);

    void logout(String refreshToken);

    void sendPasswordResetLink(@Valid FindPasswordRequest request);

    void resetPassword(@Valid ResetPasswordRequest request);

    void verifyEmail(String token);
}
