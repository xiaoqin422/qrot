package cn.stuxx.controller;

import cn.stuxx.config.annotation.LogPrint;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
public class WebController {
    @LogPrint
    @RequestMapping("/test")
    public String test() {
        return "Hello,Jenkins!";
    }
}
