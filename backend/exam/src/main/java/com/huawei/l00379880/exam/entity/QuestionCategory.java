/***********************************************************
 * @Description : 题目的类别表，从内容角度划分，比如数学、语文、英语等
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:46
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class QuestionCategory {

    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionCategoryId;

    @JsonProperty("name")
    private String questionCategoryName;

    @JsonProperty("description")
    private String questionCategoryDescription;
}
