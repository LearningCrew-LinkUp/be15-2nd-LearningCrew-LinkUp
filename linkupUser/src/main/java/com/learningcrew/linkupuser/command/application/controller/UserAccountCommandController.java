package com.learningcrew.linkupuser.command.application.controller;


import com.learningcrew.linkupuser.command.application.dto.ProfileUpdateRequest;
import com.learningcrew.linkupuser.command.application.dto.request.UserCreateRequest;
import com.learningcrew.linkupuser.command.application.dto.request.UserRecoverRequestDTO;
import com.learningcrew.linkupuser.command.application.dto.request.WithdrawUserRequest;
import com.learningcrew.linkupuser.command.application.dto.response.RegisterResponse;
import com.learningcrew.linkupuser.command.application.service.AccountCommandService;
import com.learningcrew.linkupuser.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "내 계정", description = "프로필, 탈퇴, 계정 복구 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserAccountCommandController {

    private final AccountCommandService userCommandService;

    /* 회원 가입 */
    @PostMapping("/register")
    @Operation(
            summary = "회원가입", description = "이메일과 비밀번호, 전화번호 등의 정보를 입력하여 회원으로 가입할 수 있다."
    )
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody UserCreateRequest request) {
        log.info("회원가입 요청: email={}, nickname={}, contact={}",
                request.getEmail(), request.getNickname(), request.getContactNumber());

        RegisterResponse response = userCommandService.registerUser(request);

        log.info("회원가입 완료: userId={}, email={}", response.getUserId(), response.getEmail());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "회원 가입 요청에 성공했습니다. 이메일 인증을 완료해주세요."));
    }

    /* 사업자 회원가입 */
    @PostMapping("/register-business")
    @Operation(
            summary = "회원가입", description = "이메일과 비밀번호, 전화번호 등의 정보를 입력하여 회원으로 가입할 수 있다."
    )
    public ResponseEntity<ApiResponse<RegisterResponse>> businessRegister(@Valid @RequestBody UserCreateRequest request) {

        RegisterResponse response = userCommandService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "회원 가입 요청에 성공했습니다. 이메일 인증을 완료해주세요."));
    }

    /* 회원 탈퇴 */
    @DeleteMapping("/withdraw")
    @Operation(
            summary = "회원탈퇴", description = "회원 id로 탈퇴할 수 있다."
    )
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal String userId, @RequestBody WithdrawUserRequest request) {
        log.info("회원탈퇴 요청: password={}",
                request.getPassword());
        userCommandService.withdrawUser(request.getPassword(), Integer.parseInt(userId));
        log.info("회원탈퇴 요청 성공");
        return ResponseEntity.ok(ApiResponse.success(null,"회원이 탈퇴되었습니다."));
    }

    /* 계정 복구 */
    @PostMapping("/recover")
    @Operation(
            summary = "계정복구", description = "계정을 복구할 수 있다."
    )
    public ResponseEntity<ApiResponse<Void>> recoverUser(@RequestBody UserRecoverRequestDTO request){
        userCommandService.recoverUser(request.getEmail(),request.getPassword());
        return ResponseEntity.ok(ApiResponse.success(null,"계정이 복구되었습니다."));
    }


    /* 프로필 수정 */
    @Operation(summary = "프로필 수정", description = "프로필을 수정할 수 있다. ")
    @PutMapping("/me/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@AuthenticationPrincipal String userId, @RequestBody ProfileUpdateRequest request){
        userCommandService.updateProfile(Integer.parseInt(userId), request);
        return ResponseEntity.ok(ApiResponse.success(null, "프로필이 수정되었습니다. "));
    }


}
