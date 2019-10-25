/***********************************************************
 * @Description : 考试记录VO
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2019/10/25 7:42
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.huawei.l00379880.exam.entity.Exam;
import com.huawei.l00379880.exam.entity.ExamRecord;
import com.huawei.l00379880.exam.entity.User;
import lombok.Data;

@Data
public class ExamRecordVo {
    /**
     * 当前考试记录对应的考试
     */
    private Exam exam;

    /**
     * 当前考试对应的内容
     */
    private ExamRecord examRecord;

    /**
     * 参加考试的用户信息
     */
    private User user;
}
