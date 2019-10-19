/***********************************************************
 * @Description : 考试题目表
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:46
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@DynamicUpdate
public class Question {
    @Id
    private String questionId;
    private String questionName;
    private Integer questionScore;
    private String questionCreatorId;
    private Integer questionLevelId;
    private Integer questionTypeId;
    private Integer questionCategoryId;
    private String questionDescription;
    private String questionOptionIds;
    private String questionAnswerOptionIds;
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
