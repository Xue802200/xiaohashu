package com.quanxiaoha.xiaohashu.auth.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @NacosValue(value = "${rate-limit.api.limit}",autoRefreshed = true)
    private Integer limit;


    @GetMapping("/test")
    public String TestController() {
        return limit.toString();
    }
}
