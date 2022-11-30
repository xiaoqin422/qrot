package cn.stuxx.utils;

import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.bot.OriginBotManager;
import love.forte.simbot.definition.Contact;
import love.forte.simbot.definition.Group;
import love.forte.simbot.message.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NotifyUserUtil {

    public static String notifyWithQQ(String accountCode, Message message) throws ExecutionException, InterruptedException {
        OriginBotManager manager = OriginBotManager.INSTANCE;
        AtomicReference<String> botCode = new AtomicReference<>("");
        for (BotManager<?> botManager : manager) {
            for (Bot bot : botManager.all()) {
                Contact friend = bot.getContact(Identifies.ID(accountCode));
                if (friend != null) {
                    friend.sendAsync(message).thenAccept((a) -> botCode.set(bot.getId().toString())).get();
                    return botCode.get();
                }
            }
        }
        return botCode.get();
    }

    public static String notifyWithQQGroup(String groupCode, Message message) {
        OriginBotManager manager = OriginBotManager.INSTANCE;
        AtomicReference<String> botCode = new AtomicReference<>("");
        for (BotManager<?> botManager : manager) {
            for (Bot bot : botManager.all()) {
                Group group = bot.getGroup(Identifies.ID(groupCode));
                if (group != null) {
                    group.sendAsync(message).thenAccept((a) -> botCode.set(bot.getId().toString()));
                    return botCode.get();
                }
            }
        }
        return botCode.get();
    }
}
