package io.devlog.devlog.user.domain.repository;

import io.devlog.devlog.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE User u SET u.enabled = true WHERE u.email= :email")
    void setEnabledByEmail(@Param("email") String email);

}