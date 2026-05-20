package com.projectjy.domain.auth.repository;

import com.projectjy.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
