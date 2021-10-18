package com.krishlab.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krishlab.entity.UserEntity;
import com.krishlab.exception.ResourceNotFoundException;
import com.krishlab.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	//@PostConstruct
	//public void initRolesAndUsers() {
	//	userService.initRolesAndUser();
	//}
	
	@PostMapping("/register")
	public UserEntity registerNewUser(@RequestBody UserEntity user) {
		return userService.registerNewUser(user);
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_DEFAULT_USER')") //enable @EnableGlobalMethodSecurity(prePostEnabled = true)
	public List<UserEntity> getUsers() {
		return this.userService.getUsers();
	}
	
	@GetMapping("/user/{id}")
	public UserEntity getUserById(@PathVariable (value = "id") long userId) {
		return this.userService.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
	}
}
