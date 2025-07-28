package com.quanxiaoha.xiaohashu.auth.service.impl;

import com.quanxiaoha.framework.common.exception.BizException;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.framework.common.util.JsonUtils;
import com.quanxiaoha.xiaohashu.auth.constant.RedisKeyConstants;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserDO;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserDOMapper;
import com.quanxiaoha.xiaohashu.auth.enums.LoginTypeEnum;
import com.quanxiaoha.xiaohashu.auth.enums.ResponseCodeEnum;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import com.quanxiaoha.xiaohashu.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 用户登陆和注册功能
     * @param userLoginReqVO
     * @return
     */
    @Override
    public Response<String> LoginAndRegister(UserLoginReqVO userLoginReqVO) {
        //获取密码和手机号
        Integer type = userLoginReqVO.getType();
        String phone = userLoginReqVO.getPhone();

        // 获取type对应的枚举
        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        if(loginTypeEnum==null){
            return null;
        }

        switch(loginTypeEnum){
            case VERIFICATION_CODE -> {
                String code = userLoginReqVO.getCode();

                //判断验证码是否为空
                if(Objects.isNull(code)){
                    return Response.fail(ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode(),"验证码不能为空");
                }

                //判断验证码是否正确
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                String redisKey =(String) redisTemplate.opsForValue().get(key);
                if(!code.equals(redisKey)){
                    return Response.fail(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                //判断用户是否注册过
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("用户是否注册,phone:{},userDO:{}",phone, JsonUtils.toJsonString(userDO));

                if(Objects.isNull(userDO)){
                    //用户第一次登陆,注册
                    //todo
                }else {
                    //注册,获取用户id
                    Long userId = userDO.getId();
                }
            }
            case PASSWORD -> {
                //todo
            }
        }


        //saToken用户登陆,返回token
        //todo


        return Response.success("");
    }
}
