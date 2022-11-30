package cn.stuxx.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskHealthResp {
    private String msg;
    private boolean isOk;
    private int status;
    private String code;
    private String address;
    private String pid;
    private String tw;

    public String response(){
        return " \t打卡结果" +
                "\n完成状态：" + status +
                "\n打卡信息：" + msg +
                "\n打卡地址：" + address +
                "\n身份证号：" + pid +
                "\n体   温：" + tw;
    }
}
