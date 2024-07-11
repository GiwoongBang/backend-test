package kr.co.polycube.backendtest.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
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

    // 매개변수와 DTO 객체 간 바인딩 실패한 경우
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex, WebRequest request) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        String reason = String.join(", ", errorMessages);
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, reason, request);
    }

    // JSON 형태가 지켜지지 않은 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String reason = ex.getMostSpecificCause().getMessage();
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, reason, request);
    }

    // 일반적인 BAD_REQUEST 경우
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return getErrorResponseResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
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

    @Setter
    @Getter
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String reason;
        private String path;
    }

    private static ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(HttpStatus httpStatus, String ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setReason(ex);
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
