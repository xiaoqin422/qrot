package cn.stuxx;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.stuxx.utils.NotifyUserUtil;
import com.alibaba.fastjson2.util.UUIDUtils;
import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.Timestamp;
import love.forte.simbot.application.Application;
import love.forte.simbot.application.EventProvider;
import love.forte.simbot.application.EventProviderFactory;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.OriginBotManager;
import love.forte.simbot.definition.Contact;
import love.forte.simbot.definition.Group;
import love.forte.simbot.event.Event;
import love.forte.simbot.event.EventProcessor;
import love.forte.simbot.event.FriendMessageEvent;
import love.forte.simbot.message.*;
import love.forte.simbot.utils.item.Items;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class QRotApplicationTest {
    private NotifyUserUtil notifyUserUtil;
    @Test
    public void test(){
        List<Message.Element<?>> messageList = new ArrayList<>();
        Messages messages = new MessagesBuilder()
                .append("111").face(Identifies.ID(1)).emoji(Identifies.ID(1)).build();
        OriginBotManager.INSTANCE.forEach(manager -> {
            manager.all().forEach((bot)->{
                Contact friend = bot.getContact(Identifies.ID("858465981"));
                System.out.println(friend == null);
                System.out.printf("%s,%s,%s,%s",friend.getUsername(),friend.getId(),friend.getAvatar(),friend.getCategory().getName());
                friend.sendBlocking(messages);
            });
        });
    }
    public void testNotify(){
        OriginBotManager manager = OriginBotManager.INSTANCE;
        manager.forEach(bots -> {//获取所有管理器
            bots.all().forEach(bot -> {//获取所有机器人
                bot.getGroups().asStream().forEach(group -> {//获取机器人所在所有群
                    if (group.getId().toString().equals("759152290")) {//如果群号等于...
                        group.sendBlocking("你们好呀");
                    }
                });
                bot.getContacts().asStream().forEach(friend -> {
                    if (friend.getId().toString().equals("1257489584")) {
                        friend.sendBlocking("Sama05你好呀");
                    }
                });
            });
        });
    }
}
