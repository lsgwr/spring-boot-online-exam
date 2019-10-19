/***********************************************************
 * @Description : 考试卡片列表
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-23 19:30
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExamCardVo {
    @JsonProperty("id")
    private String examId;
    @JsonProperty("title")
    private String examName;
    @JsonProperty("avatar")
    private String examAvatar;
    @JsonProperty("content")
    private String examDescription;
    @JsonProperty("score")
    private Integer examScore;
    /**
     * 考试限制的时间，单位为分钟
     */
    @JsonProperty("elapse")
    private Integer examTimeLimit;
}
