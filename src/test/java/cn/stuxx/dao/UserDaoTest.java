package cn.stuxx.dao;

import cn.stuxx.model.entity.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void userTest_insert(){
        // User user = new User(2L,"test","123456",2,"测试用户","123456","123456@qq.com","15735111403","[1]",0,new Date(),new Date(),0);
        User user = User.generateUser("123456");
        userDao.insertOnDefault(user);
    }
    @Test
    public void userTest_queryById(){
        User search = new User();
        search.setQCode("2578908933");
        List<User> res = userDao.queryAll(search);
        System.out.println(res);
    }
    @Test
    public void userTest_updateUser(){
        User user = userDao.queryById(2L);
        User updateUser = new User();
        BeanUtils.copyProperties(user,updateUser);
        updateUser.setName("修改");
        userDao.update(updateUser);
        updateUser = userDao.queryById(updateUser.getId());
        log.debug("修改前：{}\n修改后：{}",user,updateUser);
    }
    @Test
    public void userTest_selectAll() {
        List<User> list = userDao.queryAll(null);
        Assert.isTrue(list.size() == 1, "没有用户数据");
    }
}