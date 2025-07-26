package com.quanxiaoha.xiaohashu.auth;

import com.quanxiaoha.framework.common.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class XiaohashuAuthApplicationTests {

    @Autowired
    private UserDOMapper userDOMapper;

    @Test
    void testMapper(){
        UserDO userDO = UserDO.builder()
                .username("犬小哈")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        userDOMapper.insert(userDO);
    }

    @Test
    void testSelect(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(2L);
        System.out.println(JsonUtils.toJsonString(userDO));

    }

    @Test
    void testUpdate(){
        UserDO build = UserDO.builder()
                .id(1L)
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .username("xzj")
                .build();
        userDOMapper.updateByPrimaryKey(build);
    }

    @Test
    void testDelete(){
        userDOMapper.deleteByPrimaryKey(1L);
    }
}
