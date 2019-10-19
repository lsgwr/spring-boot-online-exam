/***********************************************************
 * @Description : 用户信息的前端展示类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-26 12:59
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfoVo {

    @JsonProperty("id")
    private String userId;

    @JsonProperty("avatar")
    private String userAvatar;

    @JsonProperty("name")
    private String userNickname;

    @JsonProperty("username")
    private String userUsername;

    /**
     * 密码不用拷贝，免得泄露信息
     */
    private String password = "";

    @JsonProperty("email")
    private String userEmail;

    @JsonProperty("telephone")
    private String userPhone;

    @JsonProperty("roleId")
    private String roleName;

    @JsonProperty("welcome")
    private String userDescription;

    @JsonProperty("role")
    private RoleVo roleVo;
}
