package com.shuangshuang.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuangshuang.usercenter.mapper.UserMapper;
import com.shuangshuang.usercenter.model.domain.User;
import com.shuangshuang.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shuangshuang.usercenter.constants.UserConstant.USER_LOGIN_STATE;

/**
 * @author Administrator
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-12-20 11:09:25
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 混淆密码
    private static final String SALT = "shuangshuang";


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1L;
        }
        if (userAccount.length() < 6) {
            return -1L;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1L;
        }
        //不含特殊字符
        String regEx = "[`~!#$%^&*()+=|{}'Aa:;',\\\\[\\\\].<>/?~！@#￥%……&*（）9——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (!matcher.find()) {
            return -1L;
        }
        //密码和校验密码不同
        if (!userPassword.equals(checkPassword)) {
            return -1L;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1L;
        }
        //2. 对密码进行加密
        String handledPassword = DigestUtils.md5Hex(SALT + userPassword);
        //3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(handledPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1L;
        }
        return user.getId();
    }

    @Override
    public User userlogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 6) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        //不含特殊字符
        String regEx = "[`~!#$%^&*()+=|{}'Aa:;',\\\\[\\\\].<>/?~！@#￥%……&*（）9——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (!matcher.find()) {
            return null;
        }
        //2. 对密码进行加密
        String handledPassword = DigestUtils.md5Hex(SALT + userPassword);
        //3. 查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("password", handledPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match with userPassword");
            return null;
        }
        User safetyUser = getSafetyUser(user);

        //记录用户的登录态
        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);


        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user){
        //用户脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(0);
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setRole(user.getRole());

        return safetyUser;
    }
}




