package cn.stuxx.model.base.http;

import cn.stuxx.exception.ErrorMessage;

import java.io.Serializable;

/**
 * @author bobo
 * @Description:
 * @date 2019-03-11 11:47
 */
public class HttpBaseResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public HttpBaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public static HttpBaseResponse ok(Object o) {
        return new HttpBaseResponse(0, "成功", o);
    }

    public static <T> HttpBaseResponse<T> OK(T t) {
        return new HttpBaseResponse(0, "成功", t);
    }

    public static HttpBaseResponse error(ErrorMessage errorMessage) {
        return new HttpBaseResponse(errorMessage.getCode(), errorMessage.getMessage(), null);
    }

    public static <T> HttpBaseResponse<T> error(ErrorMessage errorMessage, T data) {
        return new HttpBaseResponse(errorMessage.getCode(), errorMessage.getMessage(), data);
    }

    public static HttpBaseResponse error(Integer code, String message) {
        return new HttpBaseResponse(code, message, null);
    }

    public static <T> HttpBaseResponse<T> error(Integer code, String message, T data) {
        return new HttpBaseResponse(code, message, data);
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
