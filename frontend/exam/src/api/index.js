const api = {
  Login: '/auth/login',
  Logout: '/auth/logout',
  ForgePassword: '/auth/forge-password',
  Register: '/auth/register',
  twoStepCode: '/auth/2step-code',
  SendSms: '/account/sms',
  SendSmsErr: '/account/sms_err',
  // get my info
  UserInfo: '/user/info',

  // 下面是自己的用户认证的接口
  UserRegister: '/user/register',
  UserLogin: '/user/login',

  // 考试的接口
  ExamQuestionList: '/exam/question/list',
  ExamQuestionAll: '/question/question/',
  ExamQuestionUpdate: '/exam/question/update',
  ExamQuestionSelection: '/question/selection',
  ExamQuestionCreate: '/exam/question/create',
  ExamList: '/exam/list',
  ExamAll: '/exam/exam/all/',
  // 获取问题列表，按照单选、多选和判断进行分类
  ExamQuestionTypeList: '/question/type/list',
  ExamCreate: '/exam/exam/',
  ExamUpdate: '/exam/update',
  ExamCardList: '/exam/exam/list/',
  // 获取考试详情
  ExamDetail: '/exam/exam/',
  // 获取考试详情
  QuestionDetail: '/question/question/',
  // 交卷
  FinishExam: '/exam/finish/',
  ExamRecordList: '/exam/record/',
  recordDetail: '/exam/record/'
}
export default api
