package cn.stuxx.exception;

import lombok.Data;

@Data
public class QRotException extends RuntimeException{
    private static final long serialVersionUID = 8860225995376647065L;
    private String msg;
    private int code = 500;
    public QRotException(String msg){
        super(msg);
        this.msg = msg;
    }
    public QRotException(String msg, int code){
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public QRotException(String msg, Throwable e){
        super(msg,e);
        this.msg = msg;
    }
    public QRotException(String msg, int code,Throwable e){
        super(msg,e);
        this.msg = msg;
        this.code = code;
    }
}
