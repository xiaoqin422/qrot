package cn.stuxx.controller;

import cn.stuxx.config.annotation.LogPrint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @LogPrint
    @RequestMapping("/test")
    public String test() {
        return "Hello,Jenkins!";
    }
}
