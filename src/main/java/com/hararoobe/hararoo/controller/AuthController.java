package com.hararoobe.hararoo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hararoobe.hararoo.model.LoginRequest;
import com.hararoobe.hararoo.model.ResponseVO;
import com.hararoobe.hararoo.model.SignupRequest;
import com.hararoobe.hararoo.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	
	@Autowired
	UserService userService;


	@PostMapping("/signin")
	public ResponseVO<String> registeredUser(@Valid @RequestBody LoginRequest loginRequest) {
		return userService.signin(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		return userService.registerUser(signUpRequest);
	}

	@PostMapping("/generate-email-verify-otp")
	public ResponseVO<String> generateEmailVerifyOtp(@Valid @RequestBody LoginRequest loginRequest) {
		return userService.generateEmailVerifyOtp(loginRequest);
	}

	@GetMapping("/email/verify")
	public ResponseVO<String> verifyOtp(@RequestHeader("emailId") String emailId,
			@RequestHeader("otp") Long otp) {
		return userService.verifyOtp(emailId, otp);
	}

}
