package com.quanxiaoha.xiaohashu.auth.service;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;

public interface UserService {

    /**
     * 登录和注册功能
     * @param userLoginReqVO
     * @return
     */
    Response<String> LoginAndRegister(UserLoginReqVO userLoginReqVO);


    Long registerUser(String phone);
}
