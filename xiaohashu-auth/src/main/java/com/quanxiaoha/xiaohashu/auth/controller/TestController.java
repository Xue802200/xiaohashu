package com.quanxiaoha.xiaohashu.auth.controller;

import com.quanxiaoha.xiaohashu.auth.alarm.AlarmInterface;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Resource
    private AlarmInterface alarmInterface;

    @GetMapping("/test/alarm")
    public String controller() {
        log.info("我被执行了吗");
        alarmInterface.send("有问题！");
        return "success";
    }
}
