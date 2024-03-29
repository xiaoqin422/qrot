package cn.stuxx.model.vo;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskHealthResp {
    private String code;
    private Integer status;

    private String msg;
    private String address;
    private String pid;
    private String tw;

    public String response() {
        return " \t打卡结果" +
                "\n执行时间：" + LocalDateTimeUtil.now() +
                "\n打卡信息：" + msg +
                "\n打卡地址：" + address +
                "\n身份证号：" + pid +
                "\n体   温：" + tw;
    }
}
