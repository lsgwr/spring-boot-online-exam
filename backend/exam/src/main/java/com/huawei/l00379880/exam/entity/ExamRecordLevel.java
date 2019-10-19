/***********************************************************
 * @Description : 考试得分级别
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:44
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ExamRecordLevel {
    @Id
    @GeneratedValue
    private Integer examRecordLevelId;
    private String examRecordLevelName;
    private String examRecordLevelDescription;
}
