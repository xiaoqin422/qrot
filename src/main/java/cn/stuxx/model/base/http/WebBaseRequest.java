package cn.stuxx.model.base.http;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WebBaseRequest {
    @NotNull(message = "用户信息不能为空")
    private Long id;
}
