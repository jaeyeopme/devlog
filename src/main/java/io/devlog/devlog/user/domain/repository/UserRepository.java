package io.devlog.devlog.user.domain.repository;

import io.devlog.devlog.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findByEmail(String email);

}