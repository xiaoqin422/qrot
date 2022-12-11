package cn.stuxx.utils;

import cn.stuxx.exception.ErrorMessage;
import cn.stuxx.exception.QRotException;
import cn.stuxx.model.entity.TaskNotify;
import cn.stuxx.utils.http.HttpClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TaskHealthListenUtil {
    private static final String CLASS_URL = "yx.ty-ke.com/Teacher/Tiwenxinxi/tiwenxinxi_data";
    private static final String STU_URL = "yx.ty-ke.com/Teacher/Tiwenxinxi/tiwenxinxi_list";
    private static final String LOGIN_URL = "yx.ty-ke.com//Teacher/Login/login_data";

    public static TaskNotify getUnFinishMsg(Map<String, JSONObject> students, String username, String password, String classCode) {
        StringBuilder msg = new StringBuilder();
        Map<String, Object> teacher = loginAndGetUser(username, password);
        String token = teacher.remove("token").toString();
        String classFinishMsg = getClassFinishMsg(classCode, token, teacher);
        Map<String, Object> unFinishMsg = getStuMsg(token, teacher);
        // 人员全部都完成
        if (unFinishMsg.size() == 0) {
            return null;
        }
        msg.append("\t").append(Constant.CommentMsg.TASK_LISTEN)
                .append("\n打卡完成度：").append(classFinishMsg)
                .append("\n未打卡人员：");
        // 群聊中被监听的班级成员
        List<String> atLists = new LinkedList<>();
        for (Map.Entry<String, Object> entry : unFinishMsg.entrySet()) {
            String sno = entry.getKey();
            JSONObject stu = students.get(sno);
            if (stu != null) {
                String accountCode = (String) stu.get("stuCode");
                String name = (String) stu.get("stuName");
                msg.append(name).append(" ");
                atLists.add(accountCode);
            }
        }
        // 如果群聊中所有班级成员已经打卡 则不发送该消息
        if (atLists.size() != 0) {
            msg.append("\n打卡不规范，开学两行泪。请大家及时完成打卡呦~\n");
            TaskNotify notify = new TaskNotify();
            notify.setMsg(msg.toString());
            notify.setAtList(atLists);
            return notify;
        }
        return null;
    }

    private static Map<String, Object> getStuMsg(String token, Map<String, Object> param) {
        log.info("获取班级学生打卡信息。token:{}", token);
        Map<String, Object> resp = HttpClient.handleBaseResponse(HttpClient.doAuthPost(STU_URL, param, token), new TypeReference<Map<String, Object>>() {
        });
        String code = String.valueOf(resp.get("code"));
        if (Strings.isBlank(code) || !"200".equals(code)) {
            log.error("获取班级学生打卡信息出错。err：{}", resp.get("msg"));
            throw new QRotException(ErrorMessage.SYSTEM_ERROR.getMessage());
        }
        Map<String, Object> res = new HashMap<>();
        List<JSONObject> list = (List<JSONObject>) resp.get("data");
        list = list.stream().filter(object -> !isFinish(object)).collect(Collectors.toList());
        for (JSONObject object : list) {
            String stu_number = (String) object.get("stu_number");
            res.put(stu_number, object);
        }
        return res;
    }

    private static Map<String, Object> loginAndGetUser(String username, String password) {
        log.info("获取用户登录token。url:{},username:{},password:{}", LOGIN_URL, username, password);
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        Map<String, Object> resp = HttpClient.handleBaseResponse(HttpClient.doPost(LOGIN_URL, param), new TypeReference<Map<String, Object>>() {
        });
        String code = String.valueOf(resp.get("code"));
        if (Strings.isBlank(code) || !"200".equals(code)) {
            log.error("获取用户登录token出错。err：{}", resp.get("msg"));
            throw new QRotException(ErrorMessage.SYSTEM_ERROR.getMessage());
        }
        Map<String, Object> res = new HashMap<>();
        JSONObject user = (JSONObject) resp.get("res");
        res.put("token", user.get("token"));
        res.put("role_id", user.get("role_id"));
        res.put("teacher_id", user.get("id"));
        return res;
    }

    private static String getClassFinishMsg(String classCode, String token, Map<String, Object> param) {
        log.info("获取班级打卡完成情况。classCode:{}", classCode);
        Map<String, Object> resp = HttpClient.handleBaseResponse(HttpClient.doAuthPost(CLASS_URL, param, token), new TypeReference<Map<String, Object>>() {
        });
        String code = String.valueOf(resp.get("code"));
        if (Strings.isBlank(code) || !"200".equals(code)) {
            log.error("获取班级打卡完成情况出错。err：{}", resp.get("msg"));
            throw new QRotException(ErrorMessage.SYSTEM_ERROR.getMessage());
        }
        List<JSONObject> list = (List<JSONObject>) resp.get("data");
        list = list.stream().filter(object -> classCode.equals(object.get("title").toString()))
                .collect(Collectors.toList());
        JSONObject object = list.get(0);
        Object classId = object.get("id");
        Object percentage = object.get("percentage");
        Object student_num = object.get("student_num");
        Object student_wan_num = object.get("student_wan_num");
        Object shiduan1 = object.get("shiduan1");
        Object shiduan2 = object.get("shiduan2");
        Object shiduan3 = object.get("shiduan3");
        param.put("cha_id", classId);
        return percentage.toString() + "  打卡数：" + student_wan_num.toString() + "/"
                + student_num.toString() + "  打卡情况：" + shiduan1.toString() + "/" +
                shiduan2.toString() + "/" + shiduan3.toString();
    }

    private static boolean isFinish(JSONObject jsonObject) {
        Integer zao = (Integer) jsonObject.get("zao");
        Integer zhong = (Integer) jsonObject.get("zhong");
        Integer wan = (Integer) jsonObject.get("wan");
        return 1 == zao || 1 == zhong || 1 == wan;
    }
}
