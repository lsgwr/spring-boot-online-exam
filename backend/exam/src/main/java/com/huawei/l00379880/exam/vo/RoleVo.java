/***********************************************************
 * @Description : 角色的实体类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-26 13:27
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleVo {
    @JsonProperty("id")
    private String roleName;

    @JsonProperty("name")
    private String roleDescription;

    @JsonProperty("describe")
    private String roleDetail;

    @JsonProperty("permissions")
    private List<PageVo> pageVoList;
}
