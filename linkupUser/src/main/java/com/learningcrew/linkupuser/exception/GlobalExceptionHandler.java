package com.learningcrew.linkupuser.exception;


import com.learningcrew.linkupuser.exception.security.CustomJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 비즈니스 로직 예외 처리 핸들러 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("[BusinessException] {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    /* 커스텀 JwtException 예외 처리 핸들러 */
    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(CustomJwtException e) {
        log.warn("[JwtException] {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    /* Spring Security 인증 실패 예외 처리 핸들러 */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        log.warn("[BadCredentials] {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_CREDENTIALS.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_CREDENTIALS, e.getMessage()));
    }

    /* Spring Security 인가 실패 예외 처리 핸들러 */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception e) {
        log.warn("[AccessDenied] {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.FORBIDDEN_ACCESS.getStatus())
                .body(ErrorResponse.of(ErrorCode.FORBIDDEN_ACCESS));
    }

    /* 유효성 검사 예외 처리 핸들러 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("유효성 검사 실패");

        log.warn("[ValidationException] {}", errorMessage);
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, errorMessage));
    }

    /* 예상하지 못한 모든 예외 처리 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[UnhandledException] ", e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}