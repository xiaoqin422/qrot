package cn.stuxx.service;

import cn.stuxx.model.base.qrot.QRotBaseRequest;
import cn.stuxx.model.vo.TaskHealthReq;
import cn.stuxx.utils.MyValidationUtil;
import cn.stuxx.utils.ValidationGroup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class TaskHealthTest {
    @Autowired
    TaskHealthService healthService;
    @Autowired
    TaskNotifyService taskNotifyService;
    @Test
    public void taskHealthTest_doTaskHealth(){
        // Validator.isM
        TaskHealthReq req = new TaskHealthReq();
        // req.setAccountCode("2578908933");
        req.setPid("14272320010422081X");
        req.setAddress("山西省运城市芮城县风陵渡镇上阳村");
        req.setTemperature(31.0);
        MyValidationUtil.validate(req, ValidationGroup.TASK_HEALTH_INSERT.class);
        // TaskHealthResp taskResp = healthService.doHealthTask(req);
        // System.out.println(taskResp);
    }
    @Test
    public void taskHealthTest_insert(){
        TaskHealthReq req = new TaskHealthReq();
        QRotBaseRequest qRotBaseRequest = new QRotBaseRequest();
        qRotBaseRequest.setAccountCode("16431748");
        req.setQq(qRotBaseRequest);
        req.setAddress("山西省运城市芮城县风陵渡镇");
        req.setPid("142723200104220811");
        req.setTemperature(38.0);
        healthService.createHealthTaskByQQ(req);
    }
    @Test
    public void logTest(){
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }

}
