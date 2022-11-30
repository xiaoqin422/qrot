package cn.stuxx.exception;

public enum ErrorMessage {
    SYSTEM_ERROR(4000,"服务器内部错误，请联系管理员反馈"),
    PARAM_BIND_FAILED(4001,"非法请求，参数错误"),
    TASK_HEALTH_NOT_EXIST(5001,"打卡任务不存在或参数缺失"),
    TASK_HEALTH_ERROR(5002,"打卡任务执行异常"),
    ACCOUNT_CODE_ERROR(5003,"QQ号码或群聊号码有误"),
    ACCOUNT_CODE_IS_BIND_HEALTH_TASK(5004,"当前号码已经绑定打卡任务"),
    TASK_HEALTH_ADDRESS_ERROR(5005,"地址格式有误"),
    TASK_HEALTH_PID_ERROR(5006,"身份证号有误"),
    TASK_HEALTH_TW_ERROR(5007,"体温数据要在[36.0,37.5]区间"),
    TASK_HEALTH_IS_EXIST(5008,"当前任务已经被创建"),
    USERNAME_IS_EXIST(5009,"用户名称已经被注册"),
    ACCOUNT_CODE_IS_EXIST(5010,"QQ账号已经被绑定"),
    ACCOUNT_CODE_NOT_EXIST(5014,"QQ账号未绑定用户"),
    USER_IS_EXIST(5011,"该用户已经被创建"),
    USER_IS_NOT_EXIST(5013,"用户不存在"),
    ;
    private Integer code;
    private String msg;

    ErrorMessage(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public ErrorMessage setMessage(String message) {
        this.msg = message;
        return this;
    }
}
