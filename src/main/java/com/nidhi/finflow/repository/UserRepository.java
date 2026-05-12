package com.nidhi.finflow.repository;

import com.nidhi.finflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
