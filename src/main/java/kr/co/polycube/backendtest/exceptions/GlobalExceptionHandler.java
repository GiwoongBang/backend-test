package kr.co.polycube.backendtest.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 에러 처리
    // 매개변수 유효성 검사 실패한 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        String reason = String.join(", ", errorMessages);
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, reason, request);
    }

    // 매개변수 타입이 일치하지 않는 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String reason = ex.getMostSpecificCause().getMessage();
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, reason, request);
    }

    // JSON 형태가 지켜지지 않은 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String reason = ex.getMostSpecificCause().getMessage();
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, reason, request);
    }

    // 404 에러 처리
    // 요청한 엔드 포인트가 없는 경우
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        return getErrorResponseResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 기타 비즈니스 로직에서 발생한 에러 처리하는 경우(ex. User 조회(getUser) 시, 확인되는 유저가 없는 경우)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        return getErrorResponseResponseEntity((HttpStatus) ex.getStatusCode(), ex.getReason(), request);
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String reason;
        private String path;

        @Builder
        public ErrorResponse(LocalDateTime timestamp, int status, String reason, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.reason = reason;
            this.path = path;
        }
    }

    private static ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(HttpStatus httpStatus, String ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .reason(ex)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
