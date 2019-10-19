/***********************************************************
 * @Description : 用户表的操作类
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-14 08:30
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.l00379880.exam.repository;

import com.huawei.l00379880.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    /**
     * 根据用户名查找到合适的用户
     *
     * @param username 用户名
     * @return 唯一符合的用户(实际用户名字段已经在数据库设置unique了 ， 肯定只会返回1条)
     */
    User findByUserUsername(String username);

    /**
     * 根据用户邮箱查找合适用户
     *
     * @param email 邮箱
     * @return 唯一符合的用户(实际邮箱字段已经在数据库设置unique了 ， 肯定只会返回1条)
     */
    User findByUserEmail(String email);
}
