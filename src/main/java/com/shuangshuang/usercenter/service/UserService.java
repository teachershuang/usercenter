package com.shuangshuang.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuangshuang.usercenter.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-12-20 11:09:25
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userlogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    User getSafetyUser(User user);
}
