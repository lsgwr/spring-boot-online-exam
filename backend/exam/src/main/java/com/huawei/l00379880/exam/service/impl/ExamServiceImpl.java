/***********************************************************
 * @Description : 考试服务接口实现
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-28 08:06
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.huawei.l00379880.exam.entity.*;
import com.huawei.l00379880.exam.enums.QuestionEnum;
import com.huawei.l00379880.exam.repository.*;
import com.huawei.l00379880.exam.service.ExamService;
import com.huawei.l00379880.exam.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final ExamRecordRepository examRecordRepository;

    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final QuestionLevelRepository questionLevelRepository;

    private final QuestionTypeRepository questionTypeRepository;

    private final QuestionCategoryRepository questionCategoryRepository;

    private final QuestionOptionRepository questionOptionRepository;

    public ExamServiceImpl(QuestionRepository questionRepository, UserRepository userRepository, QuestionLevelRepository questionLevelRepository, QuestionTypeRepository questionTypeRepository, QuestionCategoryRepository questionCategoryRepository, QuestionOptionRepository questionOptionRepository, ExamRepository examRepository, ExamRecordRepository examRecordRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionLevelRepository = questionLevelRepository;
        this.questionTypeRepository = questionTypeRepository;
        this.questionCategoryRepository = questionCategoryRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.examRepository = examRepository;
        this.examRecordRepository = examRecordRepository;
    }

    @Override
    public List<QuestionVo> getQuestionAll() {
        List<Question> questionList = questionRepository.findAll();
        return getQuestionVos(questionList);
    }

    private List<QuestionVo> getQuestionVos(List<Question> questionList) {
        // 需要自定义的question列表
        List<QuestionVo> questionVoList = new ArrayList<>();
        // 循环完成每个属性的定制
        for (Question question : questionList) {
            QuestionVo questionVo = getQuestionVo(question);
            questionVoList.add(questionVo);
        }
        return questionVoList;
    }

    private QuestionVo getQuestionVo(Question question) {
        QuestionVo questionVo = new QuestionVo();
        // 先复制能复制的属性
        BeanUtils.copyProperties(question, questionVo);
        // 设置问题的创建者
        questionVo.setQuestionCreator(
                Objects.requireNonNull(
                        userRepository.findById(
                                question.getQuestionCreatorId()
                        ).orElse(null)
                ).getUserUsername());

        // 设置问题的难度
        questionVo.setQuestionLevel(
                Objects.requireNonNull(
                        questionLevelRepository.findById(
                                question.getQuestionLevelId()
                        ).orElse(null)
                ).getQuestionLevelDescription());

        // 设置题目的类别，比如单选、多选、判断等
        questionVo.setQuestionType(
                Objects.requireNonNull(
                        questionTypeRepository.findById(
                                question.getQuestionTypeId()
                        ).orElse(null)
                ).getQuestionTypeDescription());

        // 设置题目分类，比如数学、语文、英语、生活、人文等
        questionVo.setQuestionCategory(
                Objects.requireNonNull(
                        questionCategoryRepository.findById(
                                question.getQuestionCategoryId()
                        ).orElse(null)
                ).getQuestionCategoryName()
        );

        // 选项的自定义Vo列表
        List<QuestionOptionVo> optionVoList = new ArrayList<>();

        // 获得所有的选项列表
        List<QuestionOption> optionList = questionOptionRepository.findAllById(
                Arrays.asList(question.getQuestionOptionIds().split("-"))
        );

        // 获取所有的答案列表optionList中每个option的isAnswer选项
        List<QuestionOption> answerList = questionOptionRepository.findAllById(
                Arrays.asList(question.getQuestionAnswerOptionIds().split("-"))
        );

        // 根据选项和答案的id相同设置optionVo的isAnswer属性
        for (QuestionOption option : optionList) {
            QuestionOptionVo optionVo = new QuestionOptionVo();
            BeanUtils.copyProperties(option, optionVo);
            for (QuestionOption answer : answerList) {
                if (option.getQuestionOptionId().equals(answer.getQuestionOptionId())) {
                    optionVo.setAnswer(true);
                }
            }
            optionVoList.add(optionVo);
        }

        // 设置题目的所有选项
        questionVo.setQuestionOptionVoList(optionVoList);
        return questionVo;
    }

    @Override
    public QuestionVo updateQuestion(QuestionVo questionVo) {
        // 1.把需要的属性都设置好
        StringBuilder questionAnswerOptionIds = new StringBuilder();
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionVo> questionOptionVoList = questionVo.getQuestionOptionVoList();
        int size = questionOptionVoList.size();
        for (int i = 0; i < questionOptionVoList.size(); i++) {
            QuestionOptionVo questionOptionVo = questionOptionVoList.get(i);
            QuestionOption questionOption = new QuestionOption();
            BeanUtils.copyProperties(questionOptionVo, questionOption);
            questionOptionList.add(questionOption);
            if (questionOptionVo.getAnswer()) {
                if (i != size - 1) {
                    // 把更新后的答案的id加上去,记得用-连到一起
                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId()).append("-");
                } else {
                    // 最后一个不需要用-连接
                    questionAnswerOptionIds.append(questionOptionVo.getQuestionOptionId());
                }
            }
        }

        // 1.更新问题
        Question question = questionRepository.findById(questionVo.getQuestionId()).orElse(null);
        assert question != null;
        BeanUtils.copyProperties(questionVo, question);
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds.toString());
        questionRepository.save(question);

        // 2.更新所有的option
        questionOptionRepository.saveAll(questionOptionList);

        // 返回更新后的问题，方便前端局部刷新
        return getQuestionVo(question);
    }

    @Override
    public void questionCreate(QuestionCreateVo questionCreateVo) {
        // 问题创建
        Question question = new Question();
        // 把能复制的属性都复制过来
        BeanUtils.copyProperties(questionCreateVo, question);
        // 设置下questionOptionIds和questionAnswerOptionIds，需要自己用Hutool生成下
        List<QuestionOption> questionOptionList = new ArrayList<>();
        List<QuestionOptionCreateVo> questionOptionCreateVoList = questionCreateVo.getQuestionOptionCreateVoList();
        for (QuestionOptionCreateVo questionOptionCreateVo : questionOptionCreateVoList) {
            QuestionOption questionOption = new QuestionOption();
            // 设置选项的的内容
            questionOption.setQuestionOptionContent(questionOptionCreateVo.getQuestionOptionContent());
            // 设置选项的id
            questionOption.setQuestionOptionId(IdUtil.simpleUUID());
            questionOptionList.add(questionOption);
        }
        // 把选项都存起来，然后才能用于下面设置Question的questionOptionIds和questionAnswerOptionIds
        questionOptionRepository.saveAll(questionOptionList);
        String questionOptionIds = "";
        String questionAnswerOptionIds = "";
        // 经过上面的saveAll方法，所有的option的主键id都已经持久化了
        for (int i = 0; i < questionOptionCreateVoList.size(); i++) {
            // 获取指定选项
            QuestionOptionCreateVo questionOptionCreateVo = questionOptionCreateVoList.get(i);
            // 获取保存后的指定对象
            QuestionOption questionOption = questionOptionList.get(i);
            questionOptionIds += questionOption.getQuestionOptionId() + "-";
            if (questionOptionCreateVo.getAnswer()) {
                // 如果是答案的话
                questionAnswerOptionIds += questionOption.getQuestionOptionId() + "-";
            }
        }
        // 把字符串最后面的"-"给去掉
        questionAnswerOptionIds = replaceLastSeparator(questionAnswerOptionIds);
        questionOptionIds = replaceLastSeparator(questionOptionIds);
        // 设置选项id组成的字符串
        question.setQuestionOptionIds(questionOptionIds);
        // 设置答案选项id组成的字符串
        question.setQuestionAnswerOptionIds(questionAnswerOptionIds);
        // 自己生成问题的id
        question.setQuestionId(IdUtil.simpleUUID());
        // 先把创建时间和更新时间每次都取当前时间吧
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        // 保存问题到数据库
        questionRepository.save(question);
    }

    @Override
    public QuestionSelectionVo getSelections() {
        QuestionSelectionVo questionSelectionVo = new QuestionSelectionVo();
        questionSelectionVo.setQuestionCategoryList(questionCategoryRepository.findAll());
        questionSelectionVo.setQuestionLevelList(questionLevelRepository.findAll());
        questionSelectionVo.setQuestionTypeList(questionTypeRepository.findAll());

        return questionSelectionVo;
    }

    /**
     * 去除字符串最后的，防止split的时候出错
     *
     * @param str 原始字符串
     * @return
     */
    public static String trimMiddleLine(String str) {
        if (str.charAt(str.length() - 1) == '-') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public QuestionDetailVo getQuestionDetail(String id) {
        Question question = questionRepository.findById(id).orElse(null);
        QuestionDetailVo questionDetailVo = new QuestionDetailVo();
        questionDetailVo.setId(id);
        questionDetailVo.setName(question.getQuestionName());
        questionDetailVo.setDescription(question.getQuestionDescription());
        // 问题类型，单选题/多选题/判断题
        questionDetailVo.setType(
                Objects.requireNonNull(
                        questionTypeRepository.findById(
                                question.getQuestionTypeId()
                        ).orElse(null)
                ).getQuestionTypeDescription()
        );
        // 获取当前问题的选项
        String optionIdsStr = trimMiddleLine(question.getQuestionOptionIds());
        String[] optionIds = optionIdsStr.split("-");
        // 获取选项列表
        List<QuestionOption> optionList = questionOptionRepository.findAllById(Arrays.asList(optionIds));
        questionDetailVo.setOptions(optionList);
        return questionDetailVo;
    }

    @Override
    public List<ExamVo> getExamAll() {
        List<Exam> examList = examRepository.findAll();
        return getExamVos(examList);
    }

    private List<ExamVo> getExamVos(List<Exam> examList) {
        // 需要自定义的exam列表
        List<ExamVo> examVoList = new ArrayList<>();
        // 循环完成每个属性的定制
        for (Exam exam : examList) {
            ExamVo examVo = new ExamVo();
            // 先尽量复制能复制的所有属性
            BeanUtils.copyProperties(exam, examVo);
            // 设置问题的创建者
            examVo.setExamCreator(
                    Objects.requireNonNull(
                            userRepository.findById(
                                    exam.getExamCreatorId()
                            ).orElse(null)
                    ).getUserUsername()
            );

            // 获取所有单选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoRadioList上
            List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
            List<Question> radioQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsRadio().split("-"))
            );
            for (Question question : radioQuestionList) {
                ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, radioQuestionVo);
                radioQuestionVo.setChecked(true); // 考试中的问题肯定被选中的
                radioQuestionVoList.add(radioQuestionVo);
            }
            examVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

            // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoCheckList上
            List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
            List<Question> checkQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsCheck().split("-"))
            );
            for (Question question : checkQuestionList) {
                ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, checkQuestionVo);
                checkQuestionVo.setChecked(true); // 考试中的问题肯定被选中的
                checkQuestionVoList.add(checkQuestionVo);
            }
            examVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

            // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoJudgeList上
            List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
            List<Question> judgeQuestionList = questionRepository.findAllById(
                    Arrays.asList(exam.getExamQuestionIdsJudge().split("-"))
            );
            for (Question question : judgeQuestionList) {
                ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
                BeanUtils.copyProperties(question, judgeQuestionVo);
                judgeQuestionVo.setChecked(true); // 考试中的问题肯定被选中的
                judgeQuestionVoList.add(judgeQuestionVo);
            }
            examVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);

            // 把examVo加到examVoList中
            examVoList.add(examVo);
        }
        return examVoList;
    }



    @Override
    public ExamQuestionTypeVo getExamQuestionType() {
        ExamQuestionTypeVo examQuestionTypeVo = new ExamQuestionTypeVo();
        // 获取所有单选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoRadioList上
        List<ExamQuestionSelectVo> radioQuestionVoList = new ArrayList<>();
        List<Question> radioQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.RADIO.getId());
        for (Question question : radioQuestionList) {
            ExamQuestionSelectVo radioQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, radioQuestionVo);
            radioQuestionVoList.add(radioQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoRadioList(radioQuestionVoList);

        // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoCheckList上
        List<ExamQuestionSelectVo> checkQuestionVoList = new ArrayList<>();
        List<Question> checkQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.CHECK.getId());
        for (Question question : checkQuestionList) {
            ExamQuestionSelectVo checkQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, checkQuestionVo);
            checkQuestionVoList.add(checkQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoCheckList(checkQuestionVoList);

        // 获取所有多选题列表，并赋值到ExamVo的属性ExamQuestionSelectVoJudgeList上
        List<ExamQuestionSelectVo> judgeQuestionVoList = new ArrayList<>();
        List<Question> judgeQuestionList = questionRepository.findByQuestionTypeId(QuestionEnum.JUDGE.getId());
        for (Question question : judgeQuestionList) {
            ExamQuestionSelectVo judgeQuestionVo = new ExamQuestionSelectVo();
            BeanUtils.copyProperties(question, judgeQuestionVo);
            judgeQuestionVoList.add(judgeQuestionVo);
        }
        examQuestionTypeVo.setExamQuestionSelectVoJudgeList(judgeQuestionVoList);
        return examQuestionTypeVo;
    }

    @Override
    public Exam create(ExamCreateVo examCreateVo, String userId) {
        // 在线考试系统创建
        Exam exam = new Exam();
        BeanUtils.copyProperties(examCreateVo, exam);
        exam.setExamId(IdUtil.simpleUUID());
        exam.setExamCreatorId(userId);
        exam.setCreateTime(new Date());
        exam.setUpdateTime(new Date());
        // Todo:这两个日志后面是要在前端传入的，这里暂时定为当前日期
        exam.setExamStartDate(new Date());
        exam.setExamEndDate(new Date());
        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        List<ExamQuestionSelectVo> radios = examCreateVo.getRadios();
        List<ExamQuestionSelectVo> checks = examCreateVo.getChecks();
        List<ExamQuestionSelectVo> judges = examCreateVo.getJudges();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);
        // 设置各个题目的id
        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        // 计算总分数
        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        examRepository.save(exam);
        return exam;
    }

    @Override
    public Exam update(ExamVo examVo, String userId) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examVo, exam);
        exam.setExamCreatorId(userId); // 考试的更新人为最新的创建人
        exam.setUpdateTime(new Date()); // 考试的更新日期要记录下

        String radioIdsStr = "";
        String checkIdsStr = "";
        String judgeIdsStr = "";
        List<ExamQuestionSelectVo> radios = examVo.getExamQuestionSelectVoRadioList();
        List<ExamQuestionSelectVo> checks = examVo.getExamQuestionSelectVoCheckList();
        List<ExamQuestionSelectVo> judges = examVo.getExamQuestionSelectVoJudgeList();
        int radioCnt = 0, checkCnt = 0, judgeCnt = 0;
        for (ExamQuestionSelectVo radio : radios) {
            if (radio.getChecked()) {
                radioIdsStr += radio.getQuestionId() + "-";
                radioCnt++;
            }
        }
        radioIdsStr = replaceLastSeparator(radioIdsStr);
        for (ExamQuestionSelectVo check : checks) {
            if (check.getChecked()) {
                checkIdsStr += check.getQuestionId() + "-";
                checkCnt++;
            }
        }
        checkIdsStr = replaceLastSeparator(checkIdsStr);
        for (ExamQuestionSelectVo judge : judges) {
            if (judge.getChecked()) {
                judgeIdsStr += judge.getQuestionId() + "-";
                judgeCnt++;
            }
        }
        judgeIdsStr = replaceLastSeparator(judgeIdsStr);
        exam.setExamQuestionIds(radioIdsStr + "-" + checkIdsStr + "-" + judgeIdsStr);
        // 设置各个题目的id
        exam.setExamQuestionIdsRadio(radioIdsStr);
        exam.setExamQuestionIdsCheck(checkIdsStr);
        exam.setExamQuestionIdsJudge(judgeIdsStr);

        // 计算总分数
        int examScore = radioCnt * exam.getExamScoreRadio() + checkCnt * exam.getExamScoreCheck() + judgeCnt * exam.getExamScoreJudge();
        exam.setExamScore(examScore);
        examRepository.save(exam);
        return exam;
    }

    @Override
    public List<ExamCardVo> getExamCardList() {
        List<Exam> examList = examRepository.findAll();
        List<ExamCardVo> examCardVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamCardVo examCardVo = new ExamCardVo();
            BeanUtils.copyProperties(exam, examCardVo);
            examCardVoList.add(examCardVo);
        }
        return examCardVoList;
    }

    @Override
    public ExamDetailVo getExamDetail(String id) {
        Exam exam = examRepository.findById(id).orElse(null);
        ExamDetailVo examDetailVo = new ExamDetailVo();
        examDetailVo.setExam(exam);
        assert exam != null;
        examDetailVo.setRadioIds(exam.getExamQuestionIdsRadio().split("-"));
        examDetailVo.setCheckIds(exam.getExamQuestionIdsCheck().split("-"));
        examDetailVo.setJudgeIds(exam.getExamQuestionIdsJudge().split("-"));
        return examDetailVo;
    }

    @Override
    public ExamRecord judge(String userId, String examId, HashMap<String, List<String>> answersMap) {
        // 开始考试判分啦~~~
        // 1.首先获取考试对象和选项数组
        ExamDetailVo examDetailVo = getExamDetail(examId);
        Exam exam = examDetailVo.getExam();
        // 2.然后获取该考试下所有的题目信息
        List<String> questionIds = new ArrayList<>();
        // 2.1 题目id的数组
        List<String> radioIdList = Arrays.asList(examDetailVo.getRadioIds());
        List<String> checkIdList = Arrays.asList(examDetailVo.getCheckIds());
        List<String> judgeIdList = Arrays.asList(examDetailVo.getJudgeIds());
        questionIds.addAll(radioIdList);
        questionIds.addAll(checkIdList);
        questionIds.addAll(judgeIdList);
        // 2.2 每种题目的分数
        int radioScore = exam.getExamScoreRadio();
        int checkScore = exam.getExamScoreCheck();
        int judgeScore = exam.getExamScoreJudge();
        // 2.3 根据问题id的数组拿到所有的问题对象，供下面步骤用
        List<Question> questionList = questionRepository.findAllById(questionIds);
        Map<String, Question> questionMap = new HashMap<>();
        for (Question question : questionList) {
            questionMap.put(question.getQuestionId(), question);
        }
        // 3.根据正确答案和用户作答信息进行判分
        Set<String> questionIdsAnswer = answersMap.keySet();
        // 存储当前考试每个题目的得分情况
        Map<String, Integer> judgeMap = new HashMap<>();
        // 考生作答地每个题目的选项(题目和题目之间用$分隔，题目有多个选项地话用-分隔,题目和选项之间用_分隔),用于查看考试详情
        // 例子：题目1的id_作答选项1-作答选项2&题目2的id_作答选项1&题目3_作答选项1-作答选项2-作答选项3
        StringBuilder answerOptionIdsSb = new StringBuilder();
        // 用户此次考试的总分
        int totalScore = 0;
        for (String questionId : questionIdsAnswer) {
            // 获取用户作答地这个题的答案信息
            Question question = questionMap.get(questionId);
            // 获取答案选项
            String questionAnswerOptionIds = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            List<String> questionAnswerOptionIdList = Arrays.asList(questionAnswerOptionIds.split("-"));
            Collections.sort(questionAnswerOptionIdList);
            String answerStr = listConcat(questionAnswerOptionIdList);
            // 获取用户作答
            List<String> questionUserOptionIdList = answersMap.get(questionId);
            Collections.sort(questionUserOptionIdList);
            String userStr = listConcat(questionUserOptionIdList);
            // 判断questionAnswerOptionIds和answersMap里面的答案是否相等
            if (answerStr.equals(userStr)) {
                // 说明题目作答正确,下面根据题型给分
                int score = 0;
                if (radioIdList.contains(questionId)) {
                    score = radioScore;
                }
                if (checkIdList.contains(questionId)) {
                    score = checkScore;
                }
                if (judgeIdList.contains(questionId)) {
                    score = judgeScore;
                }
                // 累计本次考试得分
                totalScore += score;
                // True代表题目答对
                answerOptionIdsSb.append(questionId + "@True_" + userStr + "$");
                judgeMap.put(questionId, score);
            } else {
                // 说明题目作答错误,直接判零分,False代表题目答错
                answerOptionIdsSb.append(questionId + "@False_" + userStr + "$");
                judgeMap.put(questionId, 0);
            }
        }
        // 4.计算得分，记录本次考试结果，存到ExamRecord中
        ExamRecord examRecord = new ExamRecord();
        examRecord.setExamRecordId(IdUtil.simpleUUID());
        examRecord.setExamId(examId);
        // 注意去掉最后可能有的&_-
        examRecord.setAnswerOptionIds(replaceLastSeparator(answerOptionIdsSb.toString()));
        examRecord.setExamJoinerId(userId);
        examRecord.setExamJoinDate(new Date());
        examRecord.setExamJoinScore(totalScore);
        examRecordRepository.save(examRecord);
        return examRecord;
    }

    @Override
    public List<ExamRecordVo> getExamRecordList(String userId) {
        // 获取指定用户下的考试记录列表
        List<ExamRecord> examRecordList = examRecordRepository.findByExamJoinerIdOrderByExamJoinDateDesc(userId);
        List<ExamRecordVo> examRecordVoList = new ArrayList<>();
        for (ExamRecord examRecord : examRecordList) {
            ExamRecordVo examRecordVo = new ExamRecordVo();
            Exam exam = examRepository.findById(examRecord.getExamId()).orElse(null);
            examRecordVo.setExam(exam);
            User user = userRepository.findById(userId).orElse(null);
            examRecordVo.setUser(user);
            examRecordVo.setExamRecord(examRecord);
            examRecordVoList.add(examRecordVo);
        }
        return examRecordVoList;
    }

    @Override
    public RecordDetailVo getRecordDetail(String recordId) {
        // 获取考试详情的封装对象
        ExamRecord record = examRecordRepository.findById(recordId).orElse(null);
        RecordDetailVo recordDetailVo = new RecordDetailVo();
        recordDetailVo.setExamRecord(record);
        // 用户的答案，需要解析
        HashMap<String, List<String>> answersMap = new HashMap<>();
        HashMap<String, String> resultsMap = new HashMap<>();
        assert record != null;
        String answersStr = record.getAnswerOptionIds();
        // $分隔题目,因为$在正则中有特殊用途(行尾)，所以需要括起来
        String[] questionArr = answersStr.split("[$]");
        for (String questionStr : questionArr) {
            System.out.println(questionStr);
            // 区分开题目标题和选项
            String[] questionTitleResultAndOption = questionStr.split("_");
            String[] questionTitleAndResult = questionTitleResultAndOption[0].split("@");
            String[] questionOptions = questionTitleResultAndOption[1].split("-");
            // 题目：答案选项
            answersMap.put(questionTitleAndResult[0], Arrays.asList(questionOptions));
            // 题目：True / False
            resultsMap.put(questionTitleAndResult[0], questionTitleAndResult[1]);
        }
        recordDetailVo.setAnswersMap(answersMap);
        recordDetailVo.setResultsMap(resultsMap);
        // 下面再计算正确答案的map
        ExamDetailVo examDetailVo = getExamDetail(record.getExamId());
        List<String> questionIdList = new ArrayList<>();
        questionIdList.addAll(Arrays.asList(examDetailVo.getRadioIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getCheckIds()));
        questionIdList.addAll(Arrays.asList(examDetailVo.getJudgeIds()));
        // 获取所有的问题对象
        List<Question> questionList = questionRepository.findAllById(questionIdList);
        HashMap<String, List<String>> answersRightMap = new HashMap<>();
        for (Question question : questionList) {
            // 记得去掉最后可能出现的特殊字符
            String questionAnswerOptionIdsStr = replaceLastSeparator(question.getQuestionAnswerOptionIds());
            String[] questionAnswerOptionIds = questionAnswerOptionIdsStr.split("-");
            answersRightMap.put(question.getQuestionId(), Arrays.asList(questionAnswerOptionIds));
        }
        recordDetailVo.setAnswersRightMap(answersRightMap);
        return recordDetailVo;
    }

    /**
     * 把字符串最后一个字符-替换掉
     *
     * @param str 原始字符串
     * @return 替换掉最后一个-的字符串
     */
    private String replaceLastSeparator(String str) {
        String lastChar = str.substring(str.length() - 1);
        // 题目和题目之间用$分隔，题目有多个选项地话用-分隔,题目和选项之间用_分隔
        if ("-".equals(lastChar) || "_".equals(lastChar) || "$".equals(lastChar)) {
            str = StrUtil.sub(str, 0, str.length() - 1);
        }
        return str;
    }

    /**
     * 把字符串用-连接起来
     *
     * @param strList 字符串列表
     * @return 拼接好的字符串，记住要去掉最后面的-
     */
    private String listConcat(List<String> strList) {
        StringBuilder sb = new StringBuilder();
        for (String str : strList) {
            sb.append(str);
            sb.append("-");
        }
        return replaceLastSeparator(sb.toString());
    }
}
