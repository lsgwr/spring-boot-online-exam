/***********************************************************
 * @Description : 问题的选项，适用于单选、多选和判断
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:48
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class QuestionOption {
    @Id
    private String questionOptionId;
    private String questionOptionContent;
    private String questionOptionDescription;
}
