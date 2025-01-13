package net.herit.platform.platform_spring.common.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import net.herit.platform.platform_spring.common.dto.response.ErrorResponse;
import net.herit.platform.platform_spring.common.dto.response.ErrorResponseEntity;
import net.herit.platform.platform_spring.common.logger.SourceToTarget;
import net.herit.platform.platform_spring.common.logger.call.CallLogger;
import net.herit.platform.platform_spring.common.system.ServiceInfo;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private CallLogger clg;

    /**
     * MethodArgumentNotValidException 예외처리
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> methodArgumentNotValidException(MethodArgumentNotValidException e){
        return ErrorResponseEntity.methodArgumentNotValidException(e);
    }
    
    /**
     * NoResourceFoundException 예외처리
     * @param e
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponseEntity> noResourceFoundException(NoResourceFoundException e){
        return ErrorResponseEntity.noRespNoResourceFoundExceptiononseEntity(e);
    } 

    /**
     * exception 예외처리
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> exception(Exception e){

        clg.error(SourceToTarget.Non(ServiceInfo.name), () -> e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .build();
        
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
