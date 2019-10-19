package com.huawei.l00379880.exam.service;

import cn.hutool.core.codec.Base64;
import org.junit.Test;

import static org.junit.Assert.*;

/***********************************************************
 * @Description : 用户服务测试
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2019-05-18 11:14
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
public class UserServiceTest {
    @Test
    public void base64Test(){
        String a = "伦家是一个非常长的字符串";
        String encode = Base64.encode(a);
        System.out.println(encode);
        String decodeStr = Base64.decodeStr(encode);
        System.out.println(decodeStr);
    }
}