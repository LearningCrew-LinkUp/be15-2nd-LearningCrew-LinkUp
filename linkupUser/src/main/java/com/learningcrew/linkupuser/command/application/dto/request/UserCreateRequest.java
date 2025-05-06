package com.learningcrew.linkupuser.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Schema(description = "회원 가입 요청 DTO")
public class UserCreateRequest {
    @Schema(description="이메일", example = "test@test.gmail", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다. ")
    private final String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
    @Schema(description="비밀번호", example = "StrongPwd123!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;

    @Schema(description="전화번호", example = "test@test.gmail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final String contactNumber;

    @Schema(description="회원명", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "회원명은 필수입니다.")
    private final String userName;

    @Schema(description="성별", example = "M", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "성별은 필수입니다.")
    private final String gender;

    @Size(max = 30, message = "닉네임은 최대 30자까지 가능합니다.")
    @Schema(description="닉네임", example = "닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String nickname;

    @Past(message = "생년월일이 유효하지 않습니다. ")
    @Schema(description = "생년월일", example = "YYYY-MM-DD", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final LocalDate birthDate;

    @Schema(description = "자기소개", example = "안녕하세요! 반갑습니다!", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final String introduction;

    @Schema(description = "프로필 이미지", example = "프로필 이미지 url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final String profileImageUrl;

    @Schema(description = "선호하는 운동 종류", example = "[축구, 농구]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final String[] desiredSports;
}
