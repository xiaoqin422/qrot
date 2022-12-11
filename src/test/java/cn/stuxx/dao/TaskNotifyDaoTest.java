package cn.stuxx.dao;

import cn.stuxx.model.entity.TaskNotify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TaskNotifyDaoTest {
    @Autowired
    private TaskNotifyDao taskNotifyDao;
    @Test
    public void taskNotifyTest_selectAll(){
        List<TaskNotify> list = taskNotifyDao.queryAll(null);
        for (TaskNotify notify : list) {
            for (String s : notify.getAtList()) {
                System.out.println(s);
            }
        }
    }
}
