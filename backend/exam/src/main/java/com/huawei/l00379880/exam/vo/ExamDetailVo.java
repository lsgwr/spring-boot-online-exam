/***********************************************************
 * @Description : 考试详情的实体类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-24 08:14
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExamDetailVo {
    @JsonProperty("id")
    private String examId;
    @JsonProperty("name")
    private String examName;
    @JsonProperty("avatar")
    private String examAvatar;
    @JsonProperty("desc")
    private String examDescription;

    // Todo:这里需要把考试的单选、多选和判断题提取出来

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
}
