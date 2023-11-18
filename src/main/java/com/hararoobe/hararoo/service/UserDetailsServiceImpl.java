package com.hararoobe.hararoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hararoobe.hararoo.entity.User;
import com.hararoobe.hararoo.repository.UserRepository;
import com.hararoobe.hararoo.serviceimpl.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		User user = userRepository.findByEmailId(emailId)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + emailId));

		return UserDetailsImpl.build(user);
	}
}
