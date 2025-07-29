package com.quanxiaoha.xiaohashu.auth.runner;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.quanxiaoha.framework.common.util.JsonUtils;
import com.quanxiaoha.xiaohashu.auth.constant.RedisKeyConstants;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.PermissionDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.RoleDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.RolePermissionDO;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.PermissionDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.RoleDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.RolePermissionDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// 查询出所有已启用的角色   根据角色查询出权限id   根据权限id查找具体的权限信息  组织角色id-权限集合  同步redis
@Component
@Slf4j
public class PushRolePermissions2RedisRunner implements ApplicationRunner {
    @Autowired
    private RoleDOMapper roleDOMapper;
    @Autowired
    private RolePermissionDOMapper rolePermissionDOMapper;
    @Autowired
    private PermissionDOMapper permissionDOMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //权限同步标记Key
    private static final String PUSH_PERMISSION_FLAG = "push.permission.flag";

    @Override
    public void run(ApplicationArguments args) {
        log.info("服务启动,开始同步权限信息到redis中...");

        try {
            Boolean canPush = redisTemplate.opsForValue().setIfAbsent(PUSH_PERMISSION_FLAG, "1", 1, TimeUnit.DAYS);

            if(Boolean.FALSE.equals(canPush)){
                log.info("权限信息已经同步到redis中...");
                return;
            }

            //查询所有的启用角色
            List<RoleDO> roleDOS = roleDOMapper.selectEnabledList();
            if(!CollectionUtils.isEmpty(roleDOS)){
                //获取角色id集合
                List<Long> roleIds = roleDOS.stream().map(RoleDO::getId).toList();

                //根据角色id获取权限信息
                List<RolePermissionDO> rolePermissionDOS = rolePermissionDOMapper.selectByRoleIds(roleIds);

                //将<角色id,权限id>封装起来
                Map<Long, List<Long>> roleIdPermissionIdsMap = rolePermissionDOS.stream().collect(
                        Collectors.groupingBy(RolePermissionDO::getRoleId,
                                Collectors.mapping(RolePermissionDO::getPermissionId, Collectors.toList()))
                );

                //查询所有被启用的权限
                List<PermissionDO> permissionDOS = permissionDOMapper.selectAppEnabledList();
                //权限ID-权限DO
                Map<Long, PermissionDO> permissionIdDOMap = permissionDOS.stream()
                        .collect(Collectors.toMap(PermissionDO::getId, permissionDO -> permissionDO));

                //组织 角色ID-权限关系
                Map<Long, List<PermissionDO>> roleIdPermissionDOMap = Maps.newHashMap();

                //统计每个角色的权限信息
                roleDOS.forEach(roleDO -> {
                    List<PermissionDO> permissionDOList = new ArrayList<>();
                    //每个角色的id
                    Long roleId = roleDO.getId();
                    //根据角色id获取到权限id
                    List<Long> permissionIds = roleIdPermissionIdsMap.get(roleId);
                    //根据权限id去获取权限信息,将所有的权限信息封装起来
                    permissionIds.forEach(permissionId -> {
                        PermissionDO permissionDO = permissionIdDOMap.get(permissionId);
                        permissionDOList.add(permissionDO);
                    });
                    roleIdPermissionDOMap.put(roleId, permissionDOList);
                });

                //同步至redis中
                roleIdPermissionDOMap.forEach((roleId, permissionIds) -> {
                    String redisKey = RedisKeyConstants.buildRolePermissionsKey(roleId);
                    redisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(permissionIds));
                });
            }

            log.info("服务启动,成功同步角色权限到redis中");
        } catch (Exception e) {
            log.info("同步角色权限到redis中失败：{}",e.getMessage());
        }

    }
}