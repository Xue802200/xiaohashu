package com.quanxiaoha.xiaohashu.auth.controller;

import com.quanxiaoha.framework.biz.operationlog.aspect.ApiOperationLog;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.User;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserDO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    @GetMapping("/test")
//    @ApiOperationLog(description = "测试接口")
//    public Response<String> test() {
//        return Response.success("Hello, 犬小哈专栏");
//    }


    @GetMapping("/test")
    @ApiOperationLog(description = "测试接口")
    public Response<User> test(@RequestBody User user) {
        return Response.success(user);
    }
}