package com.socool.soft.exception;

public enum ErrorCode {

    SYSTEM_ERROR(1000, "system error"),
    PARAMETER_ERROR(1001, "parameter error"), // 参数错误
    SQL_ERROR(1002, "sql error"), // 数据库相关错误

    REDIS_ERROR(2001, "redis error");

    private int code;
    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
