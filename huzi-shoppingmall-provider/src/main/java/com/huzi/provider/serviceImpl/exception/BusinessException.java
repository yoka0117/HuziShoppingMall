package com.huzi.provider.serviceImpl.exception;

public class BusinessException extends Exception {

    public String mss;

    public String code;

    public String getMss() {
        return mss;
    }

    public void setMss(String mss) {
        this.mss = mss;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BusinessException(String code, String message ) {
        this.mss = message;
        this.code = code;
    }
}
