package com.flux.user.repository;

import com.flux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Define query methods if needed
}
