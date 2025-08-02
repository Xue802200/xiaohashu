package com.quanxiaoha.xiaohashu.auth.alarm.impl;

import com.quanxiaoha.xiaohashu.auth.alarm.AlarmInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsAlarmerHelper implements AlarmInterface {
    @Override
    public boolean send(String message) {
        log.info("==> 【邮件告警】：{}", message);
        return true;
    }
}
