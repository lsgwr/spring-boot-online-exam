/***********************************************************
 * @Description : 登录的查询参数
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-19 20:18
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginQo {
    /**
     * 1表示用户名，2表示邮箱
     */
    private Integer loginType;
    /**
     * 用户名/邮箱的字符串
     */
    private String userInfo;
    /**
     * 用户密码
     */
    private String password;
}
