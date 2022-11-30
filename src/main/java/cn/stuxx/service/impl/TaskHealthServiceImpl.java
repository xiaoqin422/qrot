package cn.stuxx.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import cn.stuxx.dao.TaskHealthDao;
import cn.stuxx.dao.UserDao;
import cn.stuxx.exception.ErrorMessage;
import cn.stuxx.exception.QRotException;
import cn.stuxx.model.entity.TaskHealth;
import cn.stuxx.model.entity.User;
import cn.stuxx.model.eum.PERMISSION;
import cn.stuxx.model.vo.TaskHealthReq;
import cn.stuxx.model.vo.TaskHealthResp;
import cn.stuxx.service.TaskHealthService;
import cn.stuxx.service.UserService;
import cn.stuxx.utils.*;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.Identifies;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;

@Service
@Slf4j
@Validated
public class TaskHealthServiceImpl implements TaskHealthService {
    @Autowired
    private TaskHealthDao taskHealthDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailUtil emailUtil;
    @Resource(name = "jobServiceExecutor")
    private Executor jobServiceExecutor;

    @XxlJob("taskHealthHandler")
    public void AutoTaskHealthHandler(){
        List<TaskHealth> taskHealths = taskHealthDao.selectTimingTask();
        List<User> userList = userService.selectHasPermissionsUser(PERMISSION.TASK_HEALTH_TIMING.getCODE());
        List<User> rootUsers = userService.selectHasPermissionsUser(PERMISSION.ROOT.getCODE());
        Map<Long,User> map = new HashMap<>();
        userList.forEach((item)->map.put(item.getId(),item));
        rootUsers.forEach((item)->map.put(item.getId(),item));
        for (TaskHealth task : taskHealths) {
            Long userId = task.getUId();
            if (map.containsKey(userId)){
                jobServiceExecutor.execute(new asyncTaskHealth(map.get(userId),task));
            }
        }
    }
    private class asyncTaskHealth implements Runnable{
        private final User user;
        private final TaskHealth taskHealth;
        public asyncTaskHealth(User user,TaskHealth task) {
            this.user = user;
            this.taskHealth = task;
        }

        @SneakyThrows
        @Override
        public void run() {
            TaskHealthResp resp = doHealthTask(taskHealth);
            Messages message = new MessagesBuilder()
                    .face(Identifies.ID(RandomUtil.randomInt(0,221)))
                    .append(Constant.CommentMsg.HEADER)
                    .face(Identifies.ID(RandomUtil.randomInt(0,221)))
                    .append("\n")
                    .append(resp.response()).build();
            String botCode = NotifyUserUtil.notifyWithQQ(user.getQCode(), message);
            log.info("botCode{},isBlank{}",botCode,Strings.isBlank(botCode));
            if (Strings.isBlank(botCode)){
                String emailHeader = String.format(Constant.CommentMsg.NOTIFY_USER_ADD_BOT, botCode);
                String content = emailHeader + "\n" + resp.response();
                emailUtil.sendEmail(user.getEmail(),Constant.CommentMsg.EMAIL_SUBJECT, content);
            }
        }
    }
    /**
     * WEB端手动执行任务
     *
     * @param taskId 任务ID
     * @return 任务执行情况
     */
    @Override
    public TaskHealthResp doHealthTaskByTaskID(Long taskId) {
        TaskHealth health = taskHealthDao.queryById(taskId);
        return doHealthTask(health);
    }

    /**
     * QQ直接打卡
     *
     * @param accountCode QQ号码
     * @return 任务执行情况
     */
    @Override
    public TaskHealthResp doHealthTaskByQQ(String accountCode) {
        TaskHealth taskHealth = taskHealthDao.selectTaskByQCode(accountCode);
        if (taskHealth == null) {
            throw new QRotException(ErrorMessage.TASK_HEALTH_NOT_EXIST.getMessage(), ErrorMessage.TASK_HEALTH_NOT_EXIST.getCode());
        }
        return doHealthTask(taskHealth);
    }

    /**
     * 全参打卡
     *
     * @param req accountCode,address,pid
     * @return 任务执行情况
     */
    @Override
    public TaskHealthResp doHealthTask(TaskHealthReq req) {
        String accountCode = req.getQq().getAccountCode();
        User search = new User();
        search.setQCode(accountCode);
        search.setIsDelete(0);
        search.setIsDisable(0);
        List<User> list = userDao.queryAll(search);
        if (list.size() == 0) {
            User user = User.generateUser(accountCode);
            userDao.insert(user);
        }
        TaskHealth task = new TaskHealth();
        BeanUtils.copyProperties(req, task);
        return doHealthTask(task);
    }


    /**
     * 创建体温打卡任务。<b>不和QQ进行强制绑定</b><br>
     * ①参数校验（QQ号码,身份证,地址）②QQ号码是否创建③任务是否已经被绑定
     *
     * @param req 创建请求
     */
    @Override
    public void createHealthTask(TaskHealthReq req) {
        Long userId = req.getUId();
        User user = userDao.queryById(userId);
        if (user == null || user.getIsDelete() == 1 || user.getIsDisable() == 1) {
            throw new QRotException(ErrorMessage.USER_IS_NOT_EXIST.getMessage(), ErrorMessage.USER_IS_NOT_EXIST.getCode());
        }
        insertTaskHealth(req);
    }

    @Override
    public void createHealthTaskByQQ(TaskHealthReq req) {
        String accountCode = req.getQq().getAccountCode();
        User param = new User();
        param.setQCode(accountCode);
        param.setIsDelete(0);
        param.setIsDisable(0);
        List<User> userList = userDao.queryAll(param);
        //2、用户不存在，需要先进行用户创建
        if (userList.size() == 0) {
            User user = User.generateUser(accountCode);
            userService.insertUser(user);
            userList.add(user);
        }
        //3、确保QQ没有被绑定。任务没有被创建
        TaskHealth taskHealth = taskHealthDao.selectTaskByQCode(accountCode);
        if (taskHealth != null) {
            throw new QRotException(ErrorMessage.ACCOUNT_CODE_IS_BIND_HEALTH_TASK.getMessage(), ErrorMessage.ACCOUNT_CODE_IS_BIND_HEALTH_TASK.getCode());
        }
        insertTaskHealth(req);
    }

    /**
     * 查询打卡任务是否存在，并完成创建
     *
     * @param task 打卡任务
     */
    private void insertTaskHealth(TaskHealth task) throws QRotException {
        TaskHealth search = new TaskHealth();
        search.setPid(task.getPid());
        List<TaskHealth> resList = taskHealthDao.queryAll(search);
        if (resList.size() != 0) {
            throw new QRotException(ErrorMessage.TASK_HEALTH_IS_EXIST.getMessage(), ErrorMessage.TASK_HEALTH_IS_EXIST.getCode());
        }
        task.setTaskId(Constant.TableField.TASK_HEALTH_PREFIX + IdUtil.getSnowflakeNextIdStr());
        try {
            log.info("任务{}创建。", task);
            taskHealthDao.insert(task);
        } catch (DuplicateKeyException e) {
            log.error("任务{}创建失败。ErrorMsg:{}", task, e.getMessage());
            throw new QRotException(ErrorMessage.TASK_HEALTH_IS_EXIST.getMessage(), ErrorMessage.TASK_HEALTH_IS_EXIST.getCode());
        }
    }

    @Override
    public void updateHealthTaskByQQ(TaskHealthReq req) {
        String accountCode = req.getQq().getAccountCode();
        TaskHealth searchHealth = taskHealthDao.selectTaskByQCode(accountCode);
        if (searchHealth == null) {
            throw new QRotException(ErrorMessage.TASK_HEALTH_NOT_EXIST.getMessage(), ErrorMessage.TASK_HEALTH_NOT_EXIST.getCode());
        }
        req.setId(searchHealth.getId());
        taskHealthDao.update(req);
    }

    @Override
    public void updateHealthTask(TaskHealthReq req) {
        taskHealthDao.update(req);
    }

    @Override
    public void deleteHealthTaskByQQ(TaskHealthReq req) {
        String accountCode = req.getQq().getAccountCode();
        TaskHealth health = taskHealthDao.selectTaskByQCode(accountCode);
        if (health == null) {
            throw new QRotException(ErrorMessage.TASK_HEALTH_NOT_EXIST.getMessage(), ErrorMessage.TASK_HEALTH_NOT_EXIST.getCode());
        }
        taskHealthDao.deleteById(health.getId());
    }

    @Override
    public void deleteHealthTask(String taskID) {
        TaskHealth search = new TaskHealth();
        search.setTaskId(taskID);
        List<TaskHealth> resList = taskHealthDao.queryAll(search);
        if (resList.size() == 0) {
            throw new QRotException(ErrorMessage.TASK_HEALTH_NOT_EXIST.getMessage(), ErrorMessage.TASK_HEALTH_NOT_EXIST.getCode());
        }
        taskHealthDao.deleteById(resList.get(0).getId());
    }

    /**
     * 参数查询打卡任务
     *
     * @param search 打卡任务查询条件
     * @return 查询结果
     */
    @Override
    public List<TaskHealth> queryTaskHealth(TaskHealth search) {
        return taskHealthDao.queryAll(search);
    }

    private TaskHealthResp doHealthTask(TaskHealth task) throws QRotException {
        MyValidationUtil.validate(task, ValidationGroup.TASK_HEALTH_DO.class);
        String address = task.getAddress();
        double twDouble = task.getTemperature();
        String tw = "";
        String pid = task.getPid();
        boolean isRandom = task.getIsRandom() == 1;
        //tw范围不对
        if (isRandom || twDouble < 36.0 || twDouble > 38.0) {
            tw = String.format("%.1f", RandomUtil.randomDouble(36.0, 38.0));
        } else {
            tw = String.format("%.1f", twDouble);
        }
        return post(address, pid, tw);
    }

    /**
     * 体温打卡 post请求
     *
     * @param address 打卡地址
     * @param pid     身份证
     * @param tw      体温
     * @return 打卡返回数据
     */
    private TaskHealthResp post(String address, String pid, String tw) {
        TaskHealthResp res = new TaskHealthResp();
        Map<String, String> form = new HashMap<>();
        form.put("address", address);
        form.put("mobile", pid);
        form.put("title", tw);
        form.put("province", ReUtil.get(Constant.Regex.ADDRESS_RE, address, "province"));
        form.put("city", ReUtil.get(Constant.Regex.ADDRESS_RE, address, "city"));
        form.put("district", ReUtil.get(Constant.Regex.ADDRESS_RE, address, "county"));
        form.put("jk_type", "健康");
        form.put("wc_type", "否");
        form.put("jc_type", "否");
        form.put("is_verify", "0");
        form.put("shibie_type", "绿码");
        res.setTw(tw);
        res.setPid(pid);
        res.setAddress(address);
        HttpUtil.createPost(Constant.HEALTH_URL)
                .formStr(form)
                .contentType("application/x-www-form-urlencoded")
                .thenFunction((resp) -> {
                    res.setStatus(resp.getStatus());
                    res.setOk(resp.isOk());
                    String msg = "";
                    if (resp.isOk()) {
                        JSONObject parse = (JSONObject) JSONObject.parse(resp.body());
                        msg = (String) parse.get("msg");
                        res.setCode((String) parse.get("code"));
                        res.setMsg(msg);
                    }
                    return null;
                });
        return res;
    }
}
