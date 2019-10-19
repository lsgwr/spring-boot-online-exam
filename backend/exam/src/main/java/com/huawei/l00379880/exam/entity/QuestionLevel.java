/***********************************************************
 * @Description : 题目难度等级，比如难、中、易等
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:47
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class QuestionLevel {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer questionLevelId;

    @JsonProperty("name")
    private String questionLevelName;

    @JsonProperty("description")
    private String questionLevelDescription;
}
