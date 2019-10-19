/***********************************************************
 * @Description : 前端页面实体类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-26 12:30
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Page {
    @Id
    @GeneratedValue
    private Integer pageId;

    private String pageName;

    private String pageDescription;

    private String actionIds;
}
