package cn.stuxx;

import cn.hutool.core.bean.BeanUtil;
import cn.stuxx.model.vo.TaskHealthResp;
import cn.stuxx.utils.NotifyUserUtil;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.OriginBotManager;
import love.forte.simbot.definition.Contact;
import love.forte.simbot.definition.Group;
import love.forte.simbot.message.Message;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Test
    public void testBeanToMap(){
        Map<String, Object> content = new HashMap<>();
        TaskHealthResp resp = new TaskHealthResp();
        resp.setAddress("address");
        resp.setStatus(200);
        resp.setPid("123456");
        content = BeanUtil.beanToMap(resp,content,true,true);
        System.out.println(NotifyUserUtil.buildMessageWithMap("header", content, null).toString());
    }
    @Test
    public void testNotify(){
        OriginBotManager manager = OriginBotManager.INSTANCE;
        manager.forEach(bots -> {//获取所有管理器
            bots.all().forEach(bot -> {//获取所有机器人
                Group group = bot.getGroup(Identifies.ID("454062801"));
                group.getMembers().asStream().forEach((item) ->
                        System.out.println(item.getNickOrUsername() + "-" + item.getAvatar() + "-" + item.getCategory()));
            });
        });
    }
}
