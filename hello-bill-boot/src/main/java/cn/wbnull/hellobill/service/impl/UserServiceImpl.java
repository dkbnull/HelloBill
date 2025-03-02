package cn.wbnull.hellobill.service.impl;

import cn.wbnull.hellobill.common.core.dto.ApiRequest;
import cn.wbnull.hellobill.common.core.dto.ApiResponse;
import cn.wbnull.hellobill.common.security.component.JwtTokenProvider;
import cn.wbnull.hellobill.common.security.model.TokenModel;
import cn.wbnull.hellobill.db.entity.UserInfo;
import cn.wbnull.hellobill.db.service.UserInfoService;
import cn.wbnull.hellobill.dto.user.request.ChangePasswordRequest;
import cn.wbnull.hellobill.dto.user.request.LoginRequest;
import cn.wbnull.hellobill.dto.user.response.LoginResponse;
import cn.wbnull.hellobill.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户接口服务类
 *
 * @author null  2020-12-29
 * https://github.com/dkbnull/HelloBill
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public ApiResponse<LoginResponse> login(ApiRequest<LoginRequest> request) {
        LoginRequest data = request.getData();

        UserInfo userInfo = userInfoService.getUserInfo(data.getUsername());
        if (userInfo == null) {
            return ApiResponse.fail("用户名或密码错误");
        }

        String password = DigestUtils.md5Hex(data.getPassword() + userInfo.getSalt()).toUpperCase();
        if (!userInfo.getPassword().equals(password)) {
            return ApiResponse.fail("用户名或密码错误");
        }

        //再次检查大小写是否一致
        if (!userInfo.getUsername().equals(data.getUsername())) {
            return ApiResponse.fail("用户名或密码错误");
        }

        TokenModel tokenModel = TokenModel.build(userInfo.getUsername());
        String token = jwtTokenProvider.generateToken(tokenModel);

        return ApiResponse.success(LoginResponse.build(token, userInfo.getUsername()));
    }

    @Override
    public ApiResponse<Object> changePassword(ApiRequest<ChangePasswordRequest> request) {
        ChangePasswordRequest data = request.getData();

        UserInfo userInfo = userInfoService.getUserInfo(request.getUsername());
        if (userInfo == null) {
            return ApiResponse.fail("用户不存在");
        }

        if (!userInfo.getPassword().equals(DigestUtils.md5Hex(data.getOldPassword()).toUpperCase())) {
            return ApiResponse.fail("原密码错误");
        }

        String password = DigestUtils.md5Hex(data.getNewPassword() + userInfo.getSalt()).toUpperCase();
        userInfoService.updateUserInfo(request.getUsername(), password);

        return ApiResponse.success("密码修改成功");
    }
}
