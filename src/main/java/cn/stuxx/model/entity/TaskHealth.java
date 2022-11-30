package cn.stuxx.model.entity;

import cn.stuxx.utils.Constant;
import cn.stuxx.utils.ValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 体温打卡表(TaskHealth)实体类
 *
 * @author 秦笑笑
 * @since 2022-11-29 02:57:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskHealth implements Serializable {
    private static final long serialVersionUID = -43600543918242192L;
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务ID
     */
    @NotNull(message = "任务ID缺失",groups = {ValidationGroup.TASK_HEALTH_ID.class})
    private String taskId;
    /**
     * 用户ID
     */
    @NotNull(message = "任务未绑定用户",groups = {ValidationGroup.UID_OPTIONS.class})
    private Long uId;
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证不能为空",
            groups = {ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.TASK_HEALTH_UPDATE.class, ValidationGroup.TASK_HEALTH_DO.class})
    @Pattern(regexp = Constant.Regex.PID, message = "身份证有误",
            groups = {ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.TASK_HEALTH_UPDATE.class, ValidationGroup.TASK_HEALTH_DO.class})
    private String pid;
    /**
     * 打卡地址
     */
    @NotBlank(message = "打卡地址不能为空",
            groups = {ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.TASK_HEALTH_UPDATE.class, ValidationGroup.TASK_HEALTH_DO.class})
    @Pattern(regexp = Constant.Regex.ADDRESS, message = "地址有误",
            groups = {ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.TASK_HEALTH_UPDATE.class, ValidationGroup.TASK_HEALTH_DO.class})
    private String address;
    /**
     * 体温
     */
    @DecimalMin(value = "36.0", message = "体温数据要在[36.0,38.0]区间",
            groups = {ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.TASK_HEALTH_UPDATE.class})
    @DecimalMax(value = "38.0", message = "体温数据要在[36.0,38.0]区间",
            groups = {ValidationGroup.TASK_HEALTH_INSERT.class, ValidationGroup.TASK_HEALTH_UPDATE.class})
    private Double temperature;
    /**
     * 体温是否随机 1随机
     */
    private Integer isRandom;
    /**
     * 是否定时 1定时
     */
    private Integer isTiming;
}
