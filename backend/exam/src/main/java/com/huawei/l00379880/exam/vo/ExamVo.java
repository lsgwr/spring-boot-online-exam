/***********************************************************
 * @Description : 考试表，要有题目、总分数、时间限制、有效日期、创建者等字段
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:42
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/

package com.huawei.l00379880.exam.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamVo {
    @JsonProperty("id")
    private String examId;
    @JsonProperty("name")
    private String examName;
    @JsonProperty("avatar")
    private String examAvatar;
    @JsonProperty("desc")
    private String examDescription;

    @JsonProperty("radios")
    private List<ExamQuestionSelectVo> examQuestionSelectVoRadioList;

    @JsonProperty("checks")
    private List<ExamQuestionSelectVo> examQuestionSelectVoCheckList;

    @JsonProperty("judges")
    private List<ExamQuestionSelectVo> examQuestionSelectVoJudgeList;

    @JsonProperty("score")
    private Integer examScore;

    @JsonProperty("radioScore")
    private Integer examScoreRadio;

    @JsonProperty("checkScore")
    private Integer examScoreCheck;

    @JsonProperty("judgeScore")
    private Integer examScoreJudge;

    /**
     * 考试的创建人，根据id从用户表中查取姓名
     */
    @JsonProperty("creator")
    private String examCreator;

    /**
     * 考试限制的时间，单位为分钟
     */
    @JsonProperty("elapse")
    private Integer examTimeLimit;

    /**
     * 开始时间
     */
    @JsonProperty("startDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examStartDate;

    /**
     * 结束时间
     */
    @JsonProperty("endDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date examEndDate;

    /**
     * 创建时间
     */
    @JsonProperty("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonProperty("updateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
