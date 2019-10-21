/***********************************************************
 * @Description : 考试详情的实体类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-06-24 08:14
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.huawei.l00379880.exam.entity.Exam;
import lombok.Data;

@Data
public class ExamDetailVo {
    /**
     * 考试的基本信息对象
     */
    private Exam exam;

    /**
     * 单选题的id数组
     */
    private String[] radioIds;

    /**
     * 多选题的id数组
     */
    private String[] checkIds;

    /**
     * 判断题的id数组
     */
    private String[] judgeIds;

}
