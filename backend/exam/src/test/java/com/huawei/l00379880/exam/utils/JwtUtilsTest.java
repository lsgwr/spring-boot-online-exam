package com.huawei.l00379880.exam.utils;

import com.huawei.l00379880.exam.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.Test;

/***********************************************************
 * @Description : 测试JWT相关的方法
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2019-05-22 00:13
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
public class JwtUtilsTest {

    @Test
    public void genJsonWebToken() {
        User user = new User();
        user.setUserId("68042014e23c4ebea7234cb9c77cee5c");
        user.setUserAvatar("http://10.102.27.191:8888/exam/M00/00/00/CmYaTFzQaeqANuOUAAAQNykNG6Y574.png");
        user.setUserUsername("lsg");

        String token = JwtUtils.genJsonWebToken(user);
        System.out.println(token);
    }

    @Test
    public void checkJWT() {
        // 这个token就是上面生成地,当用户名不对或者token时限超期时都会校验不过
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsc2dfZXhhbSIsImlkIjoiNjgwNDIwMTRlMjNjNGViZWE3MjM0Y2I5Yzc3Y2VlNWMiLCJ1c2VybmFtZSI6ImxzZyIsImF2YXRhciI6Imh0dHA6Ly8xMC4xMDIuMjYuNzY6ODg4OC9leGFtL00wMC8wMC8wMC9DbVlhVEZ6UWFlcUFOdU9VQUFBUU55a05HNlk1NzQucG5nIiwiaWF0IjoxNTU4NDU1NjM4LCJleHAiOjE1NTg1NDIwMzh9.Et8BrEGAemMqQZUvt0Vzh87UZeQaG6f7g7ELC60qK7g";
        Claims claims = JwtUtils.checkJWT(token);
        if (claims == null) {
            System.out.println("用户篡改了token，校验失败!");
        }
        String id = (String) claims.get("id");
        System.out.println("id = " + id);
        String username = (String) claims.get("username");
        System.out.println("username = " + username);
        String avatar = (String) claims.get("avatar");
        System.out.println("avatar = " + avatar);
    }
}