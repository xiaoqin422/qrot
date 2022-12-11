package cn.stuxx.utils.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.Map;

@Slf4j
public class HttpClient {
    public static String doGet(String url) {
        HttpRequest client = HttpUtil.createGet(url);
        log.info("发送HTTP请求。method:{},url:{}", client.getMethod(), client.getUrl());
        return client.thenFunction((resp) -> {
            log.debug("HTTP请求结束。status:{},url:{},resp:{}", resp.getStatus(), client.getUrl(), resp);
            if (resp.isOk()) {
                return resp.body();
            }
            log.error("HTTP调用失败.method:{},url:{},resp:{}", client.getMethod(), client.getUrl(), resp.body());
            throw new RuntimeException("HTTP调用失败");
        });
    }

    /**
     * form表带post请求
     *
     * @param url 请求地址
     * @param map form表单
     * @return HttpResponse
     */
    public static String doPost(String url, Map<String, Object> map) {
        return doPost(url, map, "application/x-www-form-urlencoded", null);
    }

    /**
     * 不带参数post请求
     *
     * @param url 请求地址
     * @return HttpResponse
     */
    public static String doPost(String url) {
        return doPost(url, null, "application/x-www-form-urlencoded", null);
    }

    /**
     * 带auth的post请求
     *
     * @param url   请求地址
     * @param map   参数
     * @param token 用户令牌
     * @return HttpResponse
     * @throws Exception 请求异常
     */
    public static String doAuthPost(String url, Map<String, Object> map, String token) {
        return doPost(url, map, "application/x-www-form-urlencoded", token);
    }

    /**
     * 带参数的restful风格的post请求，即body整体为一个json数据.
     */
    public static String doBodyPost(String url, Map<String, Object> map) {
        return doPost(url, map, "application/json", null);
    }

    private static String doPost(String url, Map<String, Object> param, String contentType, String token) {
        HttpRequest client = HttpUtil.createPost(url);
        client.contentType(contentType);
        client.form(param);
        if (Strings.isNotBlank(token)) {
            client.auth(token);
        }
        log.info("发送HTTP请求。method:{},url:{},param:{}", client.getMethod(), client.getUrl(), param);
        return client.thenFunction((resp) -> {
            log.debug("HTTP请求结束。status:{},url:{},resp:{}", resp.getStatus(), client.getUrl(),resp);
            if (resp.isOk()) {
                return resp.body();
            }
            log.error("HTTP调用失败.method:{},url:{},resp:{}", client.getMethod(), client.getUrl(), resp.body());
            throw new RuntimeException("HTTP调用失败");
        });
    }

    public static <T> T handleBaseResponse(String resp, TypeReference<T> typeReference) {
        T res = null;
        try {
            res = JSONObject.parseObject(resp, typeReference);
        } catch (Exception e) {
            log.error("Response解析失败。resp:{}.err:{}",resp, e.getMessage());
            throw new RuntimeException("Response解析失败");
        }
        return res;
    }
}
