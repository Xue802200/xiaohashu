package com.quanxiaoha.xiaohashu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.quanxiaoha.framework.common.enums.DeletedEnum;
import com.quanxiaoha.framework.common.enums.StatusEnum;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.framework.common.util.JsonUtils;
import com.quanxiaoha.xiaohashu.auth.constant.RedisKeyConstants;
import com.quanxiaoha.xiaohashu.auth.constant.RoleConstants;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserRoleDO;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserRoleDOMapper;
import com.quanxiaoha.xiaohashu.auth.enums.LoginTypeEnum;
import com.quanxiaoha.xiaohashu.auth.enums.ResponseCodeEnum;
import com.quanxiaoha.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import com.quanxiaoha.xiaohashu.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private UserRoleDOMapper userRoleDOMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

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

        if(loginTypeEnum == null){
            return null;
        }

        Long userId = null;
        switch(loginTypeEnum){
            case VERIFICATION_CODE -> {
                String code = userLoginReqVO.getCode();

                //判断验证码是否为空
                Preconditions.checkArgument(StringUtils.isBlank(code),Response.fail("验证码为空"));

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
                    userId = registerUser(phone);
                }else {
                    //注册,获取用户id
                    userId = userDO.getId();
                }
            }
            case PASSWORD -> {
                //todo
            }
        }

        //saToken用户登陆
        StpUtil.login(userId);
        //返回Token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        //返回Token令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 注册用户
     * @param phone 用户的手机号
     * @return      用户的id
     */
    public Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 获取全局自增的小哈书 ID
                Long xiaohashuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHASHU_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .xiaohashuId(String.valueOf(xiaohashuId)) // 自动生成小红书号 ID
                        .nickname("小红薯" + xiaohashuId) // 自动生成昵称, 如：小红薯10000
                        .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                        .build();

                // 添加入库
                userDOMapper.insert(userDO);

                // 获取刚刚添加入库的用户 ID
                Long userId = userDO.getId();

                // 给该用户分配一个默认角色
                UserRoleDO userRoleDO = UserRoleDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleDOMapper.insert(userRoleDO);

                // 将该用户的角色 ID 存入 Redis 中
                List<Long> roles = Lists.newArrayList();
                roles.add(RoleConstants.COMMON_USER_ROLE_ID);
                String userRolesKey = RedisKeyConstants.buildUserRoleKey(phone);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly(); // 标记事务为回滚
                log.error("==> 系统注册用户异常: ", e);
                return null;
            }
        });
    }

}

