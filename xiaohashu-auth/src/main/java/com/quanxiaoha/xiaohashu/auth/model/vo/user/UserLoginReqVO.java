package com.quanxiaoha.xiaohashu.auth.model.vo.user;

import com.quanxiaoha.framework.common.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginReqVO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    private String phone;

    /**
     * 登陆验证码
     */
    private String code;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 登陆类型(1表示验证码登陆,2表示账号密码登陆)
     */
    private Integer type;
}
