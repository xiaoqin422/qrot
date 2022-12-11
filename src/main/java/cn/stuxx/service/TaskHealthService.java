package cn.stuxx.service;

import cn.stuxx.model.entity.TaskHealth;
import cn.stuxx.model.vo.TaskHealthReq;
import cn.stuxx.model.vo.TaskHealthResp;
import cn.stuxx.utils.ValidationGroup;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TaskHealthService {
    /**
     * WEB端手动执行任务
     * @param taskId 任务ID
     * @return 任务执行情况
     */
    TaskHealthResp doHealthTaskByTaskID(@Valid @NotNull(message = "打卡任务不存在") Long taskId);

    /**
     * QQ直接打卡
     * @param accountCode QQ号码
     * @return 任务执行情况
     */
    TaskHealthResp doHealthTaskByQQ(@Valid @NotNull(message = "用户信息缺失") String accountCode);

    /**
     * 全参打卡 accountCode,address,pid
     * @return 任务执行情况
     */
    @Validated({ValidationGroup.TASK_HEALTH_DO.class,ValidationGroup.QQ_OPTIONS.class})
    TaskHealthResp doHealthTask(@Valid @NotNull(message = "打卡任务参数缺失") TaskHealthReq req);


    /**
     * 创建体温打卡任务。<b>不和QQ进行强制绑定</b><br>
     * ①参数校验（QQ号码,身份证,地址）②QQ号码是否创建③任务是否已经被绑定
     * @param req 创建请求
     */
    @Validated({ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.UID_OPTIONS.class})
    void createHealthTask(@Valid @NotNull(message = "打卡任务参数缺失") TaskHealthReq req);

    /**
     * QQ直接创建打卡任务
     * @param req
     */
    @Validated({ValidationGroup.QQ_OPTIONS.class,ValidationGroup.TASK_HEALTH_INSERT.class})
    void createHealthTaskByQQ(@Valid @NotNull(message = "打卡任务参数缺失") TaskHealthReq req);

    /**
     * 根据QQ号码更新，<B>任务不能换绑<B/>
     * @param req accountCode、address、pid、tw
     */
    @Validated({ValidationGroup.QQ_OPTIONS.class,ValidationGroup.TASK_HEALTH_UPDATE.class})
    void updateHealthTaskByQQ(@Valid @NotNull(message = "打卡任务参数缺失") TaskHealthReq req);

    /**
     * 根据taskID更新(非主键ID)
     * @param req TaskHealthUpdateContent
     */
    @Validated({ValidationGroup.TASK_HEALTH_UPDATE.class,ValidationGroup.TASK_HEALTH_ID.class,ValidationGroup.UID_OPTIONS.class})
    void updateHealthTask(@Valid @NotNull(message = "打卡任务参数缺失") TaskHealthReq req);

    /**
     * 根据QQ删除
     * @param req accountCode
     */
    @Validated({ValidationGroup.QQ_OPTIONS.class})
    void deleteHealthTaskByQQ(@Valid @NotNull(message = "打卡任务参数缺失") TaskHealthReq req);

    /**
     * 根据任务ID删除（非主键ID）
     * @param taskID taskID
     */
    void deleteHealthTask(@Valid @NotNull(message = "任务ID不能为空") String taskID);



    /**
     * 参数查询打卡任务
     * @param search 打卡任务查询条件
     * @return 查询结果
     */
    List<TaskHealth> queryTaskHealth(@Valid @NotNull(message = "查询参数不能为空")TaskHealth search);
}
