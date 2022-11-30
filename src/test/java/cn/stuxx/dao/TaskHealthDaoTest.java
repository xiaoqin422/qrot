package cn.stuxx.dao;

import cn.stuxx.model.entity.TaskHealth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskHealthDaoTest {
    @Autowired
    private TaskHealthDao taskHealthDao;
    @Test
    public void taskHealthTest_selectByAccountCode(){
        TaskHealth taskHealth = taskHealthDao.selectTaskByQCode("2578908933");
        System.out.println(taskHealth);
        TaskHealth health = taskHealthDao.selectTaskByQCode("");
        System.out.println(health);
    }
    @Test
    public void taskHealthTest_queryAll(){
        TaskHealth search = new TaskHealth();
        search.setPid("14272320010422081X");
        taskHealthDao.queryAll(search);
    }
}
