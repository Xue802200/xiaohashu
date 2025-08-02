package com.quanxiaoha.xiaohashu.auth.alarm.impl;

import com.quanxiaoha.xiaohashu.auth.alarm.AlarmInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailAlarmHelper implements AlarmInterface {

    @Override
    public boolean send(String message) {
        log.info("==> 【短信告警】：{}", message);

        //业务逻辑

        return true;
    }
}
