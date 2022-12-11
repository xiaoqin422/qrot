package cn.stuxx.model.vo;

import cn.stuxx.model.base.qrot.QRotBaseRequest;
import cn.stuxx.model.entity.TaskHealth;
import cn.stuxx.utils.ValidationGroup;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class TaskHealthReq extends TaskHealth {
    @Valid
    @NotNull(message = "用户信息缺失", groups = {ValidationGroup.QQ_OPTIONS.class})
    private QRotBaseRequest qq;
}
