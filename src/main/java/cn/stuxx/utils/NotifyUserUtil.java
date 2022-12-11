package cn.stuxx.utils;

import cn.hutool.core.util.ReUtil;
import cn.stuxx.exception.ErrorMessage;
import cn.stuxx.exception.QRotException;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.bot.OriginBotManager;
import love.forte.simbot.definition.Contact;
import love.forte.simbot.definition.Group;
import love.forte.simbot.message.At;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.MessagesBuilder;
import org.apache.logging.log4j.util.Strings;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class NotifyUserUtil {
    public static List<String> botList = new LinkedList<>();

    public static String notifyWithQQ(String accountCode, Message message) throws QRotException {
        log.info("发送QQ私聊消息。to:{},msg:{}", accountCode, message);
        OriginBotManager manager = OriginBotManager.INSTANCE;
        LinkedList<String> bots = new LinkedList<>();
        for (BotManager<?> botManager : manager) {
            for (Bot bot : botManager.all()) {
                Contact friend = bot.getContact(Identifies.ID(accountCode));
                bots.add(bot.getId().toString());
                if (friend != null) {
                    friend.sendBlocking(message);
                    botList = bots;
                    return bot.getId().toString();
                }
            }
        }
        log.error("发送QQ私聊消息失败。to:{},nsg:{}", accountCode, ErrorMessage.BOT_SEND_ERROR.getMessage());
        throw new QRotException(ErrorMessage.BOT_SEND_ERROR.getMessage(), ErrorMessage.BOT_SEND_ERROR.getCode());
    }

    public static String notifyWithQQGroup(String groupCode, Message message) throws QRotException {
        log.info("发送QQ群聊消息。to:{},msg:{}", groupCode, message);
        OriginBotManager manager = OriginBotManager.INSTANCE;
        LinkedList<String> bots = new LinkedList<>();
        for (BotManager<?> botManager : manager) {
            for (Bot bot : botManager.all()) {
                Group group = bot.getGroup(Identifies.ID(groupCode));
                bots.add(bot.getId().toString());
                if (group != null) {
                    group.sendBlocking(message);
                    botList = bots;
                    return bot.getId().toString();
                }
            }
        }
        log.error("发送QQ群聊消息失败。to:{},nsg:{}", groupCode, ErrorMessage.BOT_SEND_ERROR.getMessage());
        throw new QRotException(ErrorMessage.BOT_SEND_ERROR.getMessage(), ErrorMessage.BOT_SEND_ERROR.getCode());
    }

    public static Message buildMessageWithMap(String header, Map<String, ?> content, List<?> atList) {
        MessagesBuilder builder = new MessagesBuilder();
        if (Strings.isNotBlank(header)) {
            builder.append(header).append("\n");
        }
        if (content != null) {
            for (Map.Entry<String, ?> entry : content.entrySet()) {
                String field = entry.getKey();
                String value = String.valueOf(entry.getValue());
                builder.append(field).append(":").append(value).append("\n");
            }
        }
        if (atList != null && !atList.isEmpty()) {
            for (Object o : atList) {
                String account = String.valueOf(o);
                if (ReUtil.isMatch(Constant.Regex.ACCOUNT_CODE, account)) {
                    builder.append(new At(Identifies.ID(account)));
                }
            }
        }
        return builder.build();
    }

    public static Message buildMessageWithString(String header, String content, List<?> atList) {
        MessagesBuilder builder = new MessagesBuilder();
        if (Strings.isNotBlank(header)) {
            builder.append(header).append("\n");
        }
        builder.append(content);
        if (atList != null && !atList.isEmpty()) {
            for (Object o : atList) {
                String account = String.valueOf(o);
                if (ReUtil.isMatch(Constant.Regex.ACCOUNT_CODE, account)) {
                    builder.append(new At(Identifies.ID(account)));
                }
            }
        }
        return builder.build();
    }
}
