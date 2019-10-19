package com.huawei.l00379880.exam.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    // 下面是本项目用到的所有错误码
    REGISTER_SUCCESS(0, "注册成功"),
    REGISTER_FAILED(-2, "注册失败"),
    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAILED(-1, "用户名或者密码错误"),
    GET_INFO_SUCCESS(0, "获取用户信息成功"),
    PARAM_ERR(1, "参数不正确"),
    PRODUCT_NOT_EXIST(10, "用户不存在"),
    PRODUCT_STOCK_ERR(11, "考试信息异常"),
    ORDER_STATUS_ERR(14, "考试状态异常"),
    ORDER_UPDATE_ERR(15, "考试更新异常"),
    ORDER_DETAIL_EMPTY(16, "用户详情为空");

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;
}
