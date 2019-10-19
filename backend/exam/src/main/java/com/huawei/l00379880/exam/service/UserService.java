/***********************************************************
 * @Description : 用户接口
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-17 08:02
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.service;

import com.huawei.l00379880.exam.dto.RegisterDTO;
import com.huawei.l00379880.exam.entity.User;
import com.huawei.l00379880.exam.qo.LoginQo;
import com.huawei.l00379880.exam.vo.UserInfoVo;
import com.huawei.l00379880.exam.vo.UserVo;

public interface UserService {
    /**
     * 注册
     *
     * @param registerDTO 注册参数
     * @return 注册成功后的用户信息
     */
    User register(RegisterDTO registerDTO);

    /**
     * 登录接口，登录成功返回token
     *
     * @param loginQo 登录参数
     * @return 成功返回token，失败返回null
     */
    String login(LoginQo loginQo);

    /**
     * 根据用户id获取用户信息
     *
     * @return 用户实体
     */
    UserVo getUserInfo(String userId);

    /**
     * 获取用户详细信息(主要是权限相关的)
     * @param userId 用户的id
     * @return 用户信息组装的实体
     */
    UserInfoVo getInfo(String userId);
}
