package net.herit.platform.platform_spring.common.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseEntity {
    private int code;
    private String error;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> methodArgumentNotValidException(MethodArgumentNotValidException e){
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        FieldError fieldError = fieldErrors.get(fieldErrors.size()-1);  // 가장 첫 번째 에러 필드
        String fieldName = fieldError.getField();   // 필드명
        Object rejectedValue = fieldError.getRejectedValue();   // 입력값

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseEntity.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    // 에러 코드 in 에러 코드 명세서
                    .error(fieldError.getCode())
                    .message(fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.")
                    .build());
    }

    public static ResponseEntity<ErrorResponseEntity> noRespNoResourceFoundExceptiononseEntity(NoResourceFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseEntity.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    // 에러 코드 in 에러 코드 명세서
                    .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                    .message("요청하신 URI를 찾을 수 없습니다.")
                    .build());
    }
}
