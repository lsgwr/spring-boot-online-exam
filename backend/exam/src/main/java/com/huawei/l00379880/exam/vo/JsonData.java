package com.huawei.l00379880.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：工具类
 *
 * @author liangshanguang
 * @date 2019-05-22 08:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码 0 表示成功，1表示处理中，-1表示失败
     */
    private Integer code;
    /**
     * 数据
     */
    private Object data;
    /**
     * 描述
     */
    private String msg;

    /**
     * 成功，传入数据
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     * 成功，传入数据
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 失败，传入描述信息
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * 失败，传入描述信息,状态码
     */
    public static JsonData buildError(String msg, Integer code) {
        return new JsonData(code, null, msg);
    }

    /**
     * 成功，传入数据,及描述信息
     *
     * @param data 自定义的数据
     * @param msg  返回给前端的消息
     * @return 给前端的消息体
     */
    public static JsonData buildSuccess(Object data, String msg) {
        return new JsonData(0, data, msg);
    }

    /**
     * 成功，传入数据,及状态码
     */
    public static JsonData buildSuccess(Object data, int code) {
        return new JsonData(code, data, null);
    }

}
