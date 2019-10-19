/***********************************************************
 * @Description : 问题类型的的枚举
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-18 12:00
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.enums;

import lombok.Getter;

@Getter
public enum QuestionEnum {

    /**
     * 问题类型
     */
    RADIO(1, "单选题"),
    CHECK(2, "多选题"),
    JUDGE(3, "判断题");


    QuestionEnum(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    private Integer id;
    private String role;
}
