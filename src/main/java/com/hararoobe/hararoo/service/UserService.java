package com.hararoobe.hararoo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
