/***********************************************************
 * @Description : 用户角色表
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019/5/14 07:49
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private String roleDetail;
    /**
     * 角色所能访问的页面的主键集合(用-连接起来字符串)
     */
    private String rolePageIds;
}
