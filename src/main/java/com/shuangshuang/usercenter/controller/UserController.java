package com.shuangshuang.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shuangshuang.usercenter.model.request.UserLoginRequest;
import com.shuangshuang.usercenter.model.request.UserRegisterRequest;
import com.shuangshuang.usercenter.service.UserService;
import com.shuangshuang.usercenter.model.domain.User;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

import static com.shuangshuang.usercenter.constants.UserConstant.ADMIN_ROLE;
import static com.shuangshuang.usercenter.constants.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userPassword = userRegisterRequest.getPassword();
        String userAccount = userRegisterRequest.getUserAccount();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);

    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return null;
        }
        String userPassword = userLoginRequest.getUserPassword();
        String userAccount = userLoginRequest.getUserAccount();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userlogin(userAccount, userPassword, httpServletRequest);
    }

    @GetMapping("/search")
    public List<User> searchUsersByUserName(String username, HttpServletRequest httpServletRequest) {
        //鉴权

        if (!isAdmin(httpServletRequest)) {
            return null;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username",username);
        }
        List<User> list = userService.list(userQueryWrapper);
        return list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    } 

    @PostMapping("/delete")
    public Boolean deleteUser(@RequestBody long id, HttpServletRequest httpServletRequest) {
        if (!isAdmin(httpServletRequest)) {
            return null;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    private Boolean isAdmin(HttpServletRequest httpServletRequest){
        //鉴权
        Object attribute = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) attribute;
        return user != null && user.getRole().equals(ADMIN_ROLE);
    }
}
