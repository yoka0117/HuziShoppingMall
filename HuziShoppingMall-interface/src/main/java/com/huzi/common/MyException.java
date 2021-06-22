package com.huzi.common;

public class MyException extends Exception {


    private String exceptionCode;
    private String msg;

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public MyException(String exceptionCode, String msg) {
        this.exceptionCode = exceptionCode;
        this.msg = msg;
    }



}
