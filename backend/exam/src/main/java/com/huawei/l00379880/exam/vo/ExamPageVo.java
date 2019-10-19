/***********************************************************
 * @Description : 考试列表获取
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-22 17:00
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamPageVo {
    /**
     * 分页时每个分页的大小
     */
    private Integer pageSize;

    /**
     * 当前是在第几页，注意要比前端传过来地小1
     */
    private Integer pageNo;

    /**
     * 一共有多少条符合条件的记录
     */
    private Long totalCount;

    /**
     * 一共有多少页
     */
    private Integer totalPage;

    /**
     * 当前页的详细数据
     */
    @JsonProperty("data")
    private List<ExamVo> examVoList;
}
