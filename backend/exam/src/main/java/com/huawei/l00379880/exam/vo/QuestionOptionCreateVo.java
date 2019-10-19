/***********************************************************
 * @Description : 问题选项的外层对象
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-02 20:23
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionOptionCreateVo {

    /**
     * 问题的内容
     */
    @JsonProperty("content")
    private String questionOptionContent;

    /**
     * 当前的选项是否是问题大答案
     */
    @JsonProperty("answer")
    private Boolean answer = false;

}
