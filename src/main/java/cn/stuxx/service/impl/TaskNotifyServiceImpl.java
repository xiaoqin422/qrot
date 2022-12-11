package cn.stuxx.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.stuxx.dao.TaskNotifyDao;
import cn.stuxx.dao.UserDao;
import cn.stuxx.exception.ErrorMessage;
import cn.stuxx.exception.QRotException;
import cn.stuxx.model.entity.TaskNotify;
import cn.stuxx.model.entity.User;
import cn.stuxx.model.eum.PERMISSION;
import cn.stuxx.model.vo.TaskNotifyReq;
import cn.stuxx.model.vo.TaskNotifyResp;
import cn.stuxx.service.TaskNotifyService;
import cn.stuxx.service.UserService;
import cn.stuxx.utils.Constant;
import cn.stuxx.utils.EmailUtil;
import cn.stuxx.utils.NotifyUserUtil;
import cn.stuxx.utils.TaskHealthListenUtil;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.message.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@Slf4j
public class TaskNotifyServiceImpl implements TaskNotifyService {
    @Autowired
    private TaskNotifyDao taskNotifyDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailUtil emailUtil;

    @XxlJob("taskNotifyHandler")
    public void AutoTaskNotifyHandler() {
        // 获取参数
        String taskId = XxlJobHelper.getJobParam();
        TaskNotify taskNotify = taskNotifyDao.selectTimingTaskById(taskId);
        if (taskNotify != null) {
            log.info("执行定时通知任务。task:{}", taskNotify);
            boolean isRoot = userService.userHasPermission(PERMISSION.ROOT.getCODE(), taskNotify.getUId());
            boolean hasTimingPermission = userService.userHasPermission(PERMISSION.TASK_HEALTH_LISTEN_TIMING.getCODE(), taskNotify.getUId());
            if (!isRoot && !hasTimingPermission) {
                log.error("执行定时通知任务失败。error:{}", ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING);
                throw new QRotException(ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getMessage(), ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getCode());
            }
            TaskNotifyReq req = new TaskNotifyReq();
            BeanUtils.copyProperties(taskNotify, req);
            doNotifyTask(req);
        }
    }

    @XxlJob("taskHealthNotifyHandler")
    public void AutoTaskNotifyHealthHandler() {
        // 获取参数
        String taskId = XxlJobHelper.getJobParam();
        TaskNotify taskNotify = taskNotifyDao.selectTimingTaskById(taskId);
        if (taskNotify != null) {
            log.info("执行定时体温打卡监听任务。task:{}", taskNotify);
            boolean isRoot = userService.userHasPermission(PERMISSION.ROOT.getCODE(), taskNotify.getUId());
            boolean hasTimingPermission = userService.userHasPermission(PERMISSION.TASK_HEALTH_LISTEN_TIMING.getCODE(), taskNotify.getUId());
            if (!isRoot && !hasTimingPermission) {
                log.error("执行定时体温打卡监听任务失败。error:{}", ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING);
                throw new QRotException(ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getMessage(), ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getCode());
            }
            TaskNotify task = doTaskHealthNotify(taskNotify.getMsg());
            if (task != null) {
                taskNotify.setMsg(task.getMsg());
                taskNotify.setAtList(task.getAtList());
            }else {
                Long uId = taskNotify.getUId();
                User user = userDao.queryById(uId);
                taskNotify.setReceiveCode(user.getQCode());
                taskNotify.setMsg("所有人员打卡任务执行成功！");
            }
            TaskNotifyReq req = new TaskNotifyReq();
            BeanUtils.copyProperties(taskNotify, req);
            doNotifyTask(req);
            log.info("定时体温打卡监听任务执行完成。task:{}", task);
        }
    }

    @Override
    public void createNotifyTask(TaskNotifyReq req) {
        TaskNotify task = new TaskNotify();
        BeanUtils.copyProperties(req, task);
        insertNotifyTask(task);
    }

    @Override
    public void createNotifyTaskByQQ(TaskNotifyReq req) {
        String accountCode = req.getQq().getAccountCode();
        User search = new User();
        search.setQCode(accountCode);
        List<User> list = userDao.queryAll(search);
        if (list == null) {
            search = userService.insertUser(User.generateUser(accountCode));
        }
        req.setUId(search.getId());
        TaskNotify task = new TaskNotify();
        BeanUtils.copyProperties(req, task);
        insertNotifyTask(task);
    }

    @Override
    public void deleteNotifyTask(String taskId) {
        TaskNotify search = new TaskNotify();
        search.setTaskId(taskId);
        List<TaskNotify> list = taskNotifyDao.queryAll(search);
        if (list.size() == 0) {
            log.error("删除通知任务失败。taskId:{},err:{}",taskId,ErrorMessage.TASK_NOTIFY_NOT_EXIST.getMessage());
            throw new QRotException(ErrorMessage.TASK_NOTIFY_NOT_EXIST.getMessage(), ErrorMessage.TASK_NOTIFY_NOT_EXIST.getCode());
        }
        taskNotifyDao.deleteById(list.get(0).getId());
    }

    @Override
    public void updateNotifyTask(TaskNotifyReq req) {
        String taskId = req.getTaskId();
        TaskNotify search = new TaskNotify();
        search.setTaskId(taskId);
        List<TaskNotify> list = taskNotifyDao.queryAll(search);
        if (list.size() == 0) {
            log.error("修改通知任务失败。task:{},err:{}",req,ErrorMessage.TASK_NOTIFY_NOT_EXIST.getMessage());
            throw new QRotException(ErrorMessage.TASK_NOTIFY_NOT_EXIST.getMessage(), ErrorMessage.TASK_NOTIFY_NOT_EXIST.getCode());
        }
        if (req.getIsTiming() != null && req.getIsTiming() == 1 && userService.userHasPermission(PERMISSION.TASK_HEALTH_LISTEN_TIMING.getCODE(), list.get(0).getUId())) {
            log.error("修改通知任务失败。task:{},err:{}",req,ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getMessage());
            throw new QRotException(ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getMessage(), ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getCode());
        }
        taskNotifyDao.update(req);
    }

    private void insertNotifyTask(TaskNotify task) {
        task.setTaskId(Constant.TableField.TASK_NOTIFY_PREFIX + IdUtil.getSnowflakeNextIdStr());
        if (task.getIsTiming() != null && task.getIsTiming() == 1 && userService.userHasPermission(PERMISSION.TASK_HEALTH_LISTEN_TIMING.getCODE(), task.getUId())) {
            log.error("修改通知任务失败。task:{},err:{}",task,ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getMessage());
            throw new QRotException(ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getMessage(), ErrorMessage.USER_PERMISSION_TASK_NOTIFY_TIMING.getCode());
        }
        taskNotifyDao.insert(task);
    }

    @Override
    public TaskNotifyResp doNotifyTask(TaskNotifyReq req) {
        Integer type = req.getType();
        TaskNotifyResp resp = new TaskNotifyResp();
        Message msg = NotifyUserUtil.buildMessageWithString(null, req.getMsg(), req.getAtList());
        String sendBot = "";
        resp.setStatus(200);
        try {
            if (type == 0) {
                sendBot = NotifyUserUtil.notifyWithQQ(req.getReceiveCode(), msg);
            } else {
                sendBot = NotifyUserUtil.notifyWithQQGroup(req.getReceiveCode(), msg);
            }
        } catch (QRotException e) {
            log.error("QQ通知异常。code:{},msg:{}", e.getCode(), e.getMsg());
            resp.setCode(e.getCode());
            resp.setStatus(400);
            resp.setMsg(e.getMsg());
            //异常邮件通知
            Long uId = req.getUId();
            if (uId != null) {
                User user = userDao.queryById(uId);
                String emailHeader = String.format(Constant.CommentMsg.NOTIFY_USER_ADD_BOT, NotifyUserUtil.botList);
                String content = emailHeader + "\n" + req.getMsg() + "\nErrorMsg:" + resp.getMsg();
                emailUtil.sendEmail(user.getEmail(), Constant.CommentMsg.EMAIL_SUBJECT, content);
            }
        }
        resp.setSendBot(sendBot);
        return resp;
    }

    public TaskNotify doTaskHealthNotify(String confStr) {
        JSONObject conf = (JSONObject) JSONObject.parse(confStr);
        List<JSONObject> students = (List<JSONObject>) conf.get("students");
        Map<String, JSONObject> stuMap = new HashMap<>();
        for (JSONObject student : students) {
            stuMap.put((String) student.get("stuNumber"), student);
        }
        JSONObject config = (JSONObject) conf.get("config");
        String username = (String) config.get("username");
        String password = (String) config.get("password");
        String classCode = (String) config.get("class");
        return TaskHealthListenUtil.getUnFinishMsg(stuMap, username, password, classCode);
    }

}
