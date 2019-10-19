/***********************************************************
 * @Description : Action的前端展示类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-26 13:50
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActionVo {
    @JsonProperty("action")
    private String actionName;

    @JsonProperty("describe")
    private String actionDescription;

    @JsonProperty("defaultCheck")
    private Boolean defaultCheck;
}
