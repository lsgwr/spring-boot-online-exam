# 在线考试系统前端介绍

## 开发环境
> 以下为我的本地环境，仅供参考

- node:10.16.3
- npm:6.9.0
- webstorm:2021.2.3

## 参考材料
- 基础框架：[ant-design-vue](https://github.com/vueComponent/ant-design-vue)
- 脚手架参考：[ant-design-vue-pro](https://github.com/vueComponent/ant-design-vue-pro)
- 表格组件：[vxe-table](https://gitee.com/xuliangzhan_admin/vxe-table/)
- 富文本组件：[wangEditor](https://github.com/wangeditor-team/wangEditor)

## 项目初始化

- Clone repo
```bash
git clone git@github.com:lsgwr/spring-boot-online-exam.git
cd spring-boot-online-exam/frontend/exam
```

- Install dependencies
```
npm install
```

- Compiles and hot-reloads for development
```
npm run serve
```

- Compiles and minifies for production
```
npm run build
```

- Lints and fixes files
```
npm run lint
```

## 关键点
### 本地调试配置后端转发
> vue.config.js中的proxy
```js
devServer: {
  // development server port 8000
  port: 8000,
  proxy: 'http://localhost:80' // 这里是后端服务的ip和端口，对应调整即可
},
```

### 配置通用的后端接口前缀
> 在`src/utils/request.js`中配置，比如后端接口一般都以/api开头，那么后端接口就配置成

```js
// 创建 axios 实例
const service = axios.create({
  baseURL: '/api', // api base_url
  timeout: 6000 // 请求超时时间
})
```
