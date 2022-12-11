package cn.stuxx.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 消息提醒表(TaskNotify)实体类
 *
 * @author 秦笑笑
 * @since 2022-12-09 00:31:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskNotify implements Serializable {
    private static final long serialVersionUID = -68245417403373430L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 用户ID（任务负责人）
     */
    private Long uId;
    /**
     * 接收账号
     */
    private String receiveCode;
    /**
     * 通知消息
     */
    private String msg;
    /**
     * at列表
     */
    private List<String> atList;
    /**
     * 通知类型 0 QQ 1 群聊
     */
    private Integer type;
    /**
     * 是否定时 1定时
     */
    private Integer isTiming;
}
