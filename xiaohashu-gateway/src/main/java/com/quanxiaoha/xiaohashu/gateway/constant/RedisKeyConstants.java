package com.quanxiaoha.xiaohashu.gateway.constant;

public class RedisKeyConstants {

    private static final String USER_ROLES_KEY_PREFIX = "user:roles:";


    private static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";

    /**
     * 构建角色对应的权限集合KEY
     * @param roleKey
     * @return
     */
    public static String buildRolePermissionKey(String roleKey) {
        return ROLE_PERMISSIONS_KEY_PREFIX + roleKey;
    }

    /**
     * 构建用户-角色对应的key
     * @param userId
     * @return
     */
    public static String buildUserRoleKey(Long userId) {
        return USER_ROLES_KEY_PREFIX + userId;
    }


}
