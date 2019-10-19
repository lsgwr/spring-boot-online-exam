/***********************************************************
 * @Description : 考试表，要有题目、总分数、时间限制、有效日期、创建者等字段
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:42
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/

package com.huawei.l00379880.exam.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class Exam {
    @Id
    private String examId;
    private String examName;
    private String examAvatar;
    private String examDescription;
    private String examQuestionIds;
    private String examQuestionIdsRadio;
    private String examQuestionIdsCheck;
    private String examQuestionIdsJudge;
    private Integer examScore;
    private Integer examScoreRadio;
    private Integer examScoreCheck;
    private Integer examScoreJudge;
    private String examCreatorId;
    private Integer examTimeLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examEndDate;
    /**
     * 创建时间, 设计表时设置了自动插入当前时间，无需在Java代码中设置了
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间，设计表时设置了自动插入当前时间，无需在Java代码中设置了。
     * 同时@DynamicUpdate注解可以时间当数据库数据变化时自动更新，无需人工维护
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
