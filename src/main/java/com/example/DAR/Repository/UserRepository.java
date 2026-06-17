package com.example.DAR.Repository;


import com.example.DAR.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository  extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findUserById(int id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    List<User> findUserByUserSubscriptions(String subscription);


}
