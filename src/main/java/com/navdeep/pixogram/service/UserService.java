package com.navdeep.pixogram.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.navdeep.pixogram.model.User;
import com.navdeep.pixogram.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

	User findByEmail(String email);

	User save(UserRegistrationDto registration);

	User update(User user);

	void updatePassword(String password, Long userId);

	List<String> findAllNames();

	User findById(Long id);
}
