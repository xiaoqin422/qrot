package cn.stuxx.model.base.qrot;

import cn.stuxx.utils.Constant;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class QRotBaseRequest implements Serializable {
    @NotBlank(message = "QQ号码不能为空")
    @Pattern(regexp = Constant.Regex.ACCOUNT_CODE,message = "QQ号码有误")
    private String accountCode;
}
