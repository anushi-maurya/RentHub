

package com.renthub.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.renthub.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.renthub.entity.User;
import com.renthub.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	 @Autowired
	 private UserRepository userRepository;

	 @Autowired
	 private PasswordEncoder passwordEncoder;

	 @PostMapping("/login")
	 public Map<String, String> login(@RequestBody LoginRequest request) {

	     User user = userRepository.findByEmail(request.getEmail());

	     if (user == null) {
	         return Map.of("error", "User not found");
	     }

	     if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
	         return Map.of("error", "Invalid password");
	     }

	     String token = JwtUtil.generateToken(user.getEmail());

	     return Map.of(
	         "token", token,
	         "role", user.getRole(),
	         "id", user.getId().toString()
	     );
	 }



	}


