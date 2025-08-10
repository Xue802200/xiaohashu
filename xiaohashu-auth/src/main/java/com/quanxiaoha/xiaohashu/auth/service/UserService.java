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

    /**
     * 注册用户信息
     * @param phone
     * @return
     */
    Long registerUser(String phone);

    /**
     * 用户退出登录
     * @param
     * @return
     */
    Response<String> logout();
}
