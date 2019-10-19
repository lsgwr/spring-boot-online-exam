/***********************************************************
 * @Description : 参加考试的记录，要有考试记录的id、参与者、参与时间、耗时、得分、得分级别(另建表)
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:43
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ExamRecord {
    @Id
    private String examRecordId;
    private String examJoinerId;
    private Date examJoinDate;
    private Integer examTimeCost;
    private Integer examJoinScore;
    private Integer examResultLevel;
}
