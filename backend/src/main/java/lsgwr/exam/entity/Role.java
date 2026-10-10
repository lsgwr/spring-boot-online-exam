package lsgwr.exam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Role {
    @Id
    private Integer roleId;
    private String roleName;
    private String roleDescription;
    private String roleDetail;
    private String rolePageIds;
}
