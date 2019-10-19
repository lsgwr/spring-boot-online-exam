/***********************************************************
 * @Description : 考试接口
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-28 08:05
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.service;

import com.huawei.l00379880.exam.entity.Exam;
import com.huawei.l00379880.exam.vo.*;

import java.util.List;

public interface ExamService {
    /**
     * 获取问题的列表
     *
     * @param pageNo   页码编号
     * @param pageSize 页面大小
     * @return 页面对象
     */
    QuestionPageVo getQuestionList(Integer pageNo, Integer pageSize);

    /**
     * 根据前端传过来的问题实体更新问题和选项
     *
     * @param questionVo 问题实体
     */
    void updateQuestion(QuestionVo questionVo);

    /**
     * 问题创建
     *
     * @param questionCreateVo 问题创建实体类
     */
    void questionCreate(QuestionCreateVo questionCreateVo);

    /**
     * 获取问题的选项、分类和难度的下拉列表
     *
     * @return 选项、分类和难度的封装对象
     */
    QuestionSelectionVo getSelections();

    /**
     * 获取问题的列表
     *
     * @param pageNo   页码编号
     * @param pageSize 页面大小
     * @return 考试页面对象
     */
    ExamPageVo getExamList(Integer pageNo, Integer pageSize);

    /**
     * 获取所有问题的下拉列表，方便前端创建考试时筛选
     *
     * @return 适配前端的问题下拉列表
     */
    ExamQuestionTypeVo getExamQuestionType();

    /**
     * 根据前端组装的参数进行考试创建
     *
     * @param examCreateVo 前端组装的考试对象
     * @param userId
     * @return
     */
    Exam create(ExamCreateVo examCreateVo, String userId);

    /**
     * 获取考试卡片列表
     *
     * @return 考试卡片列表
     */
    List<ExamCardVo> getExamCardList();
}
