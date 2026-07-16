package com.renthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renthub.entity.User;
import com.renthub.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public User register(@RequestBody User user) {
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    return userRepository.save(user);
	}

	
	@PostMapping("/login")
	public Object login(@RequestBody User user) {
	    User dbUser = userRepository.findByEmailAndPassword(
	        user.getEmail(), 
	        user.getPassword()
	    );

	    if (dbUser == null) {
	        return "Invalid email or password";
	    }

	    return dbUser;
	}
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
	    return userRepository.findById(id).orElse(null);
	}

	
	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

	    User user = userRepository.findById(id).orElse(null);

	    if (user == null) {
	        return null;
	    }

	    user.setName(updatedUser.getName());
	    user.setEmail(updatedUser.getEmail());
	    user.setPhone(updatedUser.getPhone());

	    return userRepository.save(user);
	}

}
