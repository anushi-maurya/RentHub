package com.renthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.renthub.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
   User findByEmailAndPassword(String email, String password);
   
   User findByEmail(String email);

}
