package cn.stuxx;

import love.forte.simboot.annotation.Listener;
import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import love.forte.simbot.definition.Friend;
import love.forte.simbot.event.FriendMessageEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableSimbot
@MapperScan("cn.stuxx.dao")
public class QRotApplication {
    public static void main(String[] args) {
        SpringApplication.run(QRotApplication.class,args);
    }
    @Listener
    public void test(FriendMessageEvent event){
        
        Friend user = event.getUser();
        user.sendAsync(event.getMessageContent());

    }
}
