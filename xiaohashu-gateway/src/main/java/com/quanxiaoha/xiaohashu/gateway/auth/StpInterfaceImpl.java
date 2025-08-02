package com.quanxiaoha.xiaohashu.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //返回此loginId拥有的权限列表

        //todo 从redis中获取

        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        //返回此LoginId拥有的权限列表

        //todo 从redis中获取
        return List.of();
    }
}
