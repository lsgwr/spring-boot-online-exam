/***********************************************************
 * @Description : 题目类型表，从功能角度划分，比如单选、多选、判断等
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:48
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
public class QuestionType {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionTypeId;

    @JsonProperty("name")
    private String questionTypeName;

    @JsonProperty("description")
    private String questionTypeDescription;
}
