package com.shuangshuang.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册
 * @author shuangshuang
 */
@Data
public class UserLoginRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = -6787766297570044516L;
    private String userAccount;
    private String userPassword;
}
