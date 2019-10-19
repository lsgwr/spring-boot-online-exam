/***********************************************************
 * @Description : 前端的操作
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-26 12:31
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Action {
    @Id
    @GeneratedValue
    private Integer actionId;

    private String actionName;

    private String actionDescription;

    private Boolean defaultCheck;
}
