package lsgwr.exam.repo;

import lsgwr.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserUsername(String username);

    boolean existsByUserUsername(String username);

    boolean existsByUserEmail(String email);
}
