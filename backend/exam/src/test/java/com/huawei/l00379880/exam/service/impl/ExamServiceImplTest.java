package com.huawei.l00379880.exam.service.impl;

import cn.hutool.core.util.StrUtil;
import org.junit.Test;

/***********************************************************
 * @Description : 
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2019-06-23 17:14
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
public class ExamServiceImplTest {

    private String replaceLastSeperator(String str) {
        String lastChar = str.substring(str.length() - 1);
        if ("-".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }

    @Test
    public void testStrReplcae() {
        String str = "14cc31707d53433f81841058a97575da-2223b2eb8c2942459344c06ce1d3ed1d-23491b7dbbdf47dcb09ece779ff44c92-";
        String sub = StrUtil.sub(str, -2, -1);
        System.out.println(sub);
        System.out.println(replaceLastSeperator(str));
    }
}