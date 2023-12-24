package com.shuangshuang.usercenter;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void digest() {
        String bytes = DigestUtils.md5Hex("123");
        System.out.println(bytes);
    }

}
