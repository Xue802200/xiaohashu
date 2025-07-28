package com.quanxiaoha.xiaohashu.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.quanxiaoha.framework.common.exception.BizException;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.auth.config.RedisTemplateConfig;
import com.quanxiaoha.xiaohashu.auth.constant.RedisKeyConstants;
import com.quanxiaoha.xiaohashu.auth.enums.ResponseCodeEnum;
import com.quanxiaoha.xiaohashu.auth.model.vo.verificationcode.SendVerificationCodeReqVO;
import com.quanxiaoha.xiaohashu.auth.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Response<?> sendVerificationCode(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        //拿到手机号
        String phone = sendVerificationCodeReqVO.getPhone();

        //构建key
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);

        //判断redis中是否存在,存在抛出业务异常
        boolean result = redisTemplate.hasKey(key);

        if (result) {
            //请求太频繁
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        //生成六位验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        //TODO 调用第三方服务给手机发送验证码
        log.info("手机号:{},发送的验证码是:{}",phone,verificationCode);

        //存储到redis中,并设置三分钟的有效期
        redisTemplate.opsForValue().set(key,verificationCode,3, TimeUnit.MINUTES);

        return Response.success("验证码发送成功");
    }
}
