package com.shuangshuang.usercenter.service;
import java.util.Date;


import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.shuangshuang.usercenter.model.domain.User;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 *
 * @author shuangshuang
 */
@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("testShuang");
        user.setUserAccount("123456");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setPassword("123456");
        user.setPhone("123");
        user.setUserStatus(0);
        user.setEmail("123");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "shuangshuang1";
        String userPassword = "";
        String checkPassword = "shuangshuang";
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "testShuang";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "shuangshuang";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userPassword = "shuang";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "shuang  shuang";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userPassword = "shuangshuang";
        checkPassword = "shuangshuang1";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "shuangshuang";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result != -1);
    }
}