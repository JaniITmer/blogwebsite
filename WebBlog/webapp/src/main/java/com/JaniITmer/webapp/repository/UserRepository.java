package com.JaniITmer.webapp.repository;

import com.JaniITmer.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    //Login-JWT
    Optional<User> findUserByUsername(String username);
}
