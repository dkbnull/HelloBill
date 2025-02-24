package cn.wbnull.hellobill.model.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用户登录接口请求参数
 *
 * @author null  2020-12-29
 * https://github.com/dkbnull/HelloBill
 */
@Data
public class LoginRequestModel {

    @NotEmpty(message = "username 不能为空")
    private String username;

    @NotEmpty(message = "password 不能为空")
    private String password;
}
