/***********************************************************
 * @Description : 用户信息的展示类,通过JsonProperty来简化返回
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-25 20:32
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserVo {
    @JsonProperty("id")
    private String userId;

    @JsonProperty("username")
    private String userUsername;

    @JsonProperty("nickname")
    private String userNickname;

    @JsonProperty("role")
    private Integer userRoleId;

    @JsonProperty("avatar")
    private String userAvatar;

    @JsonProperty("description")
    private String userDescription;

    @JsonProperty("email")
    private String userEmail;

    @JsonProperty("phone")
    private String userPhone;
}
