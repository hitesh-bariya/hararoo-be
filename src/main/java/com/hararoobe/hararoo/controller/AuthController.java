package com.hararoobe.hararoo.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hararoobe.hararoo.common.CommonUtils;
import com.hararoobe.hararoo.config.JwtUtils;
import com.hararoobe.hararoo.entity.OtpData;
import com.hararoobe.hararoo.entity.Role;
import com.hararoobe.hararoo.entity.User;
import com.hararoobe.hararoo.enums.ERole;
import com.hararoobe.hararoo.model.LoginRequest;
import com.hararoobe.hararoo.model.MessageResponse;
import com.hararoobe.hararoo.model.ResponseVO;
import com.hararoobe.hararoo.model.SignupRequest;
import com.hararoobe.hararoo.repository.OtpDataRepository;
import com.hararoobe.hararoo.repository.RoleRepository;
import com.hararoobe.hararoo.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	OtpDataRepository otpDataRepository;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseVO<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		ResponseVO<String> response = new ResponseVO<String>();

		Boolean existsEmail = userRepository.existsByEmailId(loginRequest.getEmailId());
		if (!existsEmail) {
			response.setStatus(200);
			response.setMessage("EmailId not found");
			return response;
		}

		Boolean existsEmailWithPassword = userRepository.existsByEmailIdAndPassword(loginRequest.getEmailId(),
				CommonUtils.encode(loginRequest.getPassword()));
		if (!existsEmailWithPassword) {
			response.setStatus(200);
			response.setMessage("Invalid password");
			return response;
		}

		String jwt = jwtUtils.generateJwtToken(loginRequest.getEmailId());
		response.setBody(jwt);
		return response;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

		if (userRepository.existsByEmailId(signUpRequest.getEmailId())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = new User(signUpRequest.getUserName(), signUpRequest.getEmailId(), signUpRequest.getContactNumber(),
				CommonUtils.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/generate-email-verify-otp")
	public ResponseVO<String> generateEmailVerifyOtp(@Valid @RequestBody LoginRequest loginRequest) {

		ResponseVO<String> response = new ResponseVO<String>();

		Boolean existsEmail = userRepository.existsByEmailId(loginRequest.getEmailId());
		if (!existsEmail) {
			response.setStatus(200);
			response.setMessage("EmailId not found");
			return response;
		}

		Boolean existsEmailWithPassword = userRepository.existsByEmailIdAndPassword(loginRequest.getEmailId(),
				CommonUtils.encode(loginRequest.getPassword()));
		if (!existsEmailWithPassword) {
			response.setStatus(200);
			response.setMessage("Invalid password");
			return response;
		}
		Random random = new Random();
		long randomNumber = 100000 + random.nextInt(900000);
		OtpData otpData = new OtpData();
		otpData.setEmailId(loginRequest.getEmailId());
		otpData.setOtpCode(randomNumber);
		otpDataRepository.save(otpData);
		response.setBody(String.valueOf(randomNumber));
		return response;
	}

	@GetMapping("/email/verify")
	public ResponseVO<String> verifyOtp(@RequestHeader("emailId") String emailId,
			@RequestHeader("otp") Long otp) {
		ResponseVO<String> response = new ResponseVO<String>();

		boolean checkOtp=otpDataRepository.existsByEmailIdAndOtpCode(emailId, otp);
		if(checkOtp) {
			Optional<User> userData=userRepository.findByEmailId(emailId);
			if(userData.isPresent()) {
				User user=userData.get();
				user.setEmailVerified(true);
				userRepository.save(user);
			}else {
				response.setStatus(200);
				response.setMessage("EmailId not found");
			}
		}else {
			response.setStatus(200);
			response.setMessage("Invlid OTP");
		}
		return response;
	}

}
