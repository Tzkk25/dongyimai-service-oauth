package com.offcn.uauth.util;

/**
 * 通用的返回值
 */
public class Result<T> {

    private boolean flag ;// 是否操作成功
    private Integer code;//状态码
    private String msg ; // 描述信息
    private T data;//返回数据值


    public Result() {
        this.flag = true;
        this.code = 2000;
        this.msg = "操作成功";
    }

    public Result(boolean flag, Integer code, String msg, T data) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean flag, Integer code, String msg) {
        this.flag = flag;
        this.code = code;
        this.msg = msg;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
