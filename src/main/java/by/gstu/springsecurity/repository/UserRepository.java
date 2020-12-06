package by.gstu.springsecurity.repository;

import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.RoleType;
import by.gstu.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Transactional
    Stream<Permission> findByRole(RoleType role);
    boolean existsByUsername(String uuid);
    void deleteAllByRole(RoleType role);
}
