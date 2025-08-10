package com.quanxiaoha.xiaohashu.auth.controller;

import com.quanxiaoha.framework.biz.operationlog.aspect.ApiOperationLog;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UpdatePasswordReqVO;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import com.quanxiaoha.xiaohashu.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperationLog(description = "用户登陆/注册")
    public Response<String> loginAndRegister(@RequestBody @Validated UserLoginReqVO userLoginReqVO){
        return userService.LoginAndRegister(userLoginReqVO);
    }

    @PostMapping("/logout")
    @ApiOperationLog(description = "用户退出登陆")
    public Response<String> logout(){
        return userService.logout();
    }

    @PostMapping("/password/update")
    @ApiOperationLog(description = "用户更新密码")
    public Response<String> updatePassword(@RequestBody @Validated UpdatePasswordReqVO updatePasswordReqVO){
        return userService.updatePassword(updatePasswordReqVO);
    }
}
