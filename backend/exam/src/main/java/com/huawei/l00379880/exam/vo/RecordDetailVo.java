/***********************************************************
 * @Description : 考试记录详情的VO
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2019/10/27 15:37
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.huawei.l00379880.exam.entity.ExamRecord;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class RecordDetailVo {
    /**
     * 含有考试记录原始信息的对象
     */
    private ExamRecord examRecord;
    /**
     * 用户此次考试的作答信息, 键是题目的id，值是选项id的列表
     */
    private HashMap<String, List<String>> answersMap;

    /**
     * 用户每题作答结果的Map，键是问题的id，值是用户这题是否回答正确，True or False
     */
    private HashMap<String, String> resultsMap;

    /**
     * 正确答案，键是题目的id，值是正确答案的id组成的列表
     */
    private HashMap<String, List<String>> answersRightMap;
}
