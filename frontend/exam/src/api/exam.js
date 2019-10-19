// 考试相关的接口，包括考试、问题、选项和评分等接口

import api from './index'
import { axios } from '../utils/request'

export function getQuestionList (parameter) {
  return axios({
    url: api.ExamQuestionList,
    method: 'get',
    params: parameter
  })
}

export function questionUpdate (parameter) {
  console.log(parameter)
  return axios({
    url: api.ExamQuestionUpdate,
    method: 'post',
    data: parameter
  })
}

export function getQuestionSelection () {
  return axios({
    url: api.ExamQuestionSelection,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function questionCreate (parameter) {
  console.log(parameter)
  return axios({
    url: api.ExamQuestionCreate,
    method: 'post',
    data: parameter
  })
}

export function getExamList (parameter) {
  return axios({
    url: api.ExamList,
    method: 'get',
    params: parameter
  })
}

// 获取所有问题，按照单选、多选和判断进行分类
export function getExamQuestionTypeList () {
  return axios({
    url: api.ExamQuestionTypeList,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getExamCardList () {
  return axios({
    url: api.ExamCardList,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function examCreate (parameter) {
  console.log(parameter)
  return axios({
    url: api.ExamCreate,
    method: 'post',
    data: parameter
  })
}


