/***********************************************************
 * @Description : 响应结果的通用类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-17 07:42
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 避免返回NULL的字段
public class ResultVO<T> {


    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO() {
    }

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg = "";

    /**
     * 具体内容
     */
    private T data;
}
