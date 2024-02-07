package com.hararoobe.hararoo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hararoobe.hararoo.model.LoginRequest;
import com.hararoobe.hararoo.model.ResponseVO;
import com.hararoobe.hararoo.model.SignupRequest;

public interface UserService {

	UserDetails loadUserByUserName(String username) throws UsernameNotFoundException;

	ResponseVO<String> signin(LoginRequest loginRequest);

	ResponseEntity<?> registerUser(SignupRequest signUpRequest);

	ResponseVO<String> generateEmailVerifyOtp(LoginRequest loginRequest);

	ResponseVO<String> verifyOtp(String emailId,Long otp);
}
