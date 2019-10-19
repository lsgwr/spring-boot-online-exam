/***********************************************************
 * @Description : 用户角色的枚举
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-18 12:00
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    /**
     * 用户角色，和数据库里面的role表相对应
     */
    ADMIN(1, "管理员"),
    TEACHER(2, "教师"),
    STUDENT(3, "学生");


    RoleEnum(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    private Integer id;
    private String role;
}
