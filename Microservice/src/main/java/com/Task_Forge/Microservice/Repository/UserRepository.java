package com.Task_Forge.Microservice.Repository;

import com.Task_Forge.Microservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
//    User findByUsername(String username);
}
