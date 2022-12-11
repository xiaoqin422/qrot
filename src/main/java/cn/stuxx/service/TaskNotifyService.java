package cn.stuxx.service;

import cn.stuxx.model.vo.TaskNotifyReq;
import cn.stuxx.model.vo.TaskNotifyResp;
import cn.stuxx.utils.ValidationGroup;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface TaskNotifyService {
    @Validated({ValidationGroup.UID_OPTIONS.class, ValidationGroup.TASK_NOTIFY_INSERT.class})
    void createNotifyTask(@Valid @NotNull(message = "通知任务参数缺失") TaskNotifyReq req);

    @Validated({ValidationGroup.QQ_OPTIONS.class, ValidationGroup.TASK_NOTIFY_INSERT.class})
    void createNotifyTaskByQQ(@Valid @NotNull(message = "通知任务参数缺失") TaskNotifyReq req);

    void deleteNotifyTask(@Valid @NotBlank(message = "任务ID不能为空") String taskId);
    @Validated({ValidationGroup.TASK_NOTIFY_ID.class, ValidationGroup.TASK_NOTIFY_UPDATE.class,ValidationGroup.UID_OPTIONS.class})
    void updateNotifyTask(@Valid @NotNull(message = "通知任务参数缺失")  TaskNotifyReq req);

    @Validated({ValidationGroup.TASK_NOTIFY_DO.class})
    TaskNotifyResp doNotifyTask(@Valid @NotNull(message = "通知任务参数缺失") TaskNotifyReq req);
}
