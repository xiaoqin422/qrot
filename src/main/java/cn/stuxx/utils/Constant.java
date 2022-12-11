package cn.stuxx.utils;

public interface Constant {
    String HEALTH_URL = "yx.ty-ke.com/Home/Monitor/monitor_add";
    interface Regex{
        /**
         * 地址正则提取
         */
        String ADDRESS_RE = "(?<province>[^省]+省|.+自治区)(?<city>[^自治州]+自治州|[^市]+市|[^盟]+盟|[^地区]+地区|.+区划)(?<county>[^市]+市|[^县]+县|[^旗]+旗|.+区)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        /**
         * 身份证正则匹配
         */
        String PID = "\\d{6}(18|19|20)\\d{2}(0\\d|10|11|12)([0-2]\\d|30|31)\\d{3}[\\dXx]";
        /**
         * 地址正则匹配
         */
        String ADDRESS = ".+(省|市).+(市|区).+(区|县).*$";
        /**
         * QQ号码
         */
        String ACCOUNT_CODE = "[1-9]([0-9]{4,9})";

    }
    interface TableField{
        String TASK_HEALTH_PREFIX = "th-";
        String TASK_NOTIFY_PREFIX = "tn-";
    }
    interface CommentMsg{
        String HEADER = "---指令执行结束---";
        String NOTIFY_USER_ADD_BOT = "---QQ消息提醒异常,请添加BOT%s---";
        String EMAIL_SUBJECT = "---QRot机器人提醒---";
        String TASK_LISTEN = "---QRot体温打卡小贴士---";
    }
}
