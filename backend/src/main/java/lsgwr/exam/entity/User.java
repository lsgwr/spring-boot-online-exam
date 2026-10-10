package lsgwr.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private String userId;
    private String userUsername;
    private String userNickname;
    private String userPassword;
    private Integer userRoleId;
    @Column(columnDefinition = "LONGTEXT")
    private String userAvatar;
    @Column(length = 512)
    private String userDescription;
    private String userEmail;
    private String userPhone;
    private Integer enabled;
    private Date createTime;
    private Date updateTime;
}
