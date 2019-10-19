/***********************************************************
 * @Description : 页面的数据库操作类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-26 12:41
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.repository;

import com.huawei.l00379880.exam.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Integer> {
}
