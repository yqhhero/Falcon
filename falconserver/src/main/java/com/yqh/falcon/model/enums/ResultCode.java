package com.yqh.falcon.model.enums;

public enum ResultCode {

    OK(200,"成功"),
    CREATED(201,"创建成功"),
    N0_CONTENT(204,"资源已不存在"),
    FAIL(1,"失败"),
    NOT_FOUND(404,"未找到资源"),
    SERVER_ERROR(500,"服务器发生意外"),
    UN_AUTHORIZED(401,"未授权"),

    PARAM_VALID_FAIL(1001,"参数校验失败"),
    PARAM_IS_BLANK(1002,"参数为空"),
    PARAM_TYPE_ERROR(1003,"参数类型错误"),
    PARAM_IS_INVALID(1004,"参数无效"),


    USER_HAS_EXISTED(2005,"用户已存在"),
    USER_LOGIN_ERROR(2002,"用户不存在或密码错误"),
    USER_PASSWORD_ERROR(2003,"原密码错误");



    private Integer code;
    private String message;

    ResultCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer code(){
        return this.code;
    }

    public String message(){
        return this.message;
    }


}
