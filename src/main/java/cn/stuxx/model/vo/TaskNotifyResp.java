package cn.stuxx.model.vo;

import lombok.Data;

@Data
public class TaskNotifyResp {
    private Integer status;
    private Integer code;
    private String msg;
    /**
     * 消息的发送人
     */
    private String sendBot;

}
