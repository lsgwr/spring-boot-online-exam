/***********************************************************
 * @Description : 问题选项的自定义实体类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-01 09:45
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionOptionVo {
    @JsonProperty("id")
    private String questionOptionId;

    @JsonProperty("content")
    private String questionOptionContent;

    @JsonProperty("answer")
    private Boolean answer = false;

    @JsonProperty("description")
    private String questionOptionDescription;
}
