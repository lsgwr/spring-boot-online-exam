package com.huawei.l00379880.exam.enums;

import lombok.Getter;

/**
 * 用户账号类型的枚举
 * @author liangshanguang
 */
@Getter
public enum LoginTypeEnum {
    /**
     * 用户的账号类型，1代表用户名，2代表邮箱
     */
    USERNAME(1, "用户名"),
    EMAIL(2, "邮箱");


    LoginTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    private Integer type;
    private String name;
}
