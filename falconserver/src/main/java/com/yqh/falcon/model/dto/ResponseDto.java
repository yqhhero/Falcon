package com.yqh.falcon.model.dto;

import com.yqh.falcon.model.enums.ResultCode;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Builder
@ToString
@Accessors(chain = true)
public class ResponseDto<T> {

    private Integer code;
    private String message;
    private T data;

    private static <T> ResponseDto of(Integer code,String message,T data) {
        return ResponseDto.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static ResponseDto success(ResultCode resultCode){
        return of(resultCode.code(),resultCode.message(),null);
    }

    public static <T> ResponseDto success(ResultCode resultCode,T data){
        return of(resultCode.code(),resultCode.message(),data);
    }

    public static ResponseDto fail(ResultCode resultCode){
        return of(resultCode.code(),resultCode.message(),null);
    }

    public static <T> ResponseDto fail(ResultCode resultCode,T data){
        return of(resultCode.code(),resultCode.message(),data);
    }


}
