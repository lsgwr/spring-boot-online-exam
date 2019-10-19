/***********************************************************
 * @Description : 问题创建的实体类,简化了下
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-02 13:26
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateSimplifyVo {
    /**
     * 题目名称
     */
    @JsonProperty("name")
    private String questionName;

    @JsonProperty("desc")
    private String questionDescription;

    /**
     * 问题的难度的id
     */
    @JsonProperty("level")
    private Integer questionLevelId;

    /**
     * 问题的类型(单选、多选、判断等)
     */
    @JsonProperty("type")
    private Integer questionTypeId;

    /**
     * 题目的类别表，从内容角度划分，比如数学、语文、英语等
     */
    @JsonProperty("category")
    private Integer questionCategoryId;

    /**
     * 创建选项 里添加的内容
     */
    @JsonProperty("option")
    private String option;

    /**
     * 问题的选项列表，带上了是否是答案的true和false
     */
    @JsonProperty("options")
    private List<QuestionOptionCreateVo> questionOptionCreateVoList;
}
