package com.app.Todo.repo;

import com.app.Todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    // Spring Security needs to find a user by their username to check login
    User findByUsername(String username);

}
