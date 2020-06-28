package com.yqh.falcon.handler;

import com.yqh.falcon.exception.UnAuthorizedException;
import com.yqh.falcon.model.dto.ResponseDto;
import com.yqh.falcon.model.enums.ResultCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //JSON对象校验异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto MethodArgNotValidExceptionHandler(MethodArgumentNotValidException e ){
        StringBuilder errorInfo = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        for(int i = 0; i < bindingResult.getFieldErrors().size(); i++){
            if(i > 0){
                errorInfo.append(",");
            }
            FieldError fieldError = bindingResult.getFieldErrors().get(i);
            errorInfo.append(fieldError.getField()).append(" :").append(fieldError.getDefaultMessage());
        }
        return ResponseDto.fail(ResultCode.PARAM_VALID_FAIL,errorInfo.toString());
    }

    //请求参数校验异常处理
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseDto constraintViolationExceptionHandler(ConstraintViolationException e){
        StringBuilder errorInfo = new StringBuilder();
        String errorMessage ;

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> item : violations) {
            errorInfo.append(item.getMessage()).append(",");
        }
        errorMessage = errorInfo.toString().substring(0, errorInfo.toString().length()-1);

        return ResponseDto.fail(ResultCode.PARAM_VALID_FAIL,errorMessage);
    }

    //用户未得到授权信息异常处理
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseDto unAuthorizedExceptionHandler( ){
        return ResponseDto.fail(ResultCode.UN_AUTHORIZED);
    }

    //全局异常处理
    @ExceptionHandler(Exception.class)
    public ResponseDto exceptionHandler( ){
        return ResponseDto.fail(ResultCode.SERVER_ERROR);
    }
}
