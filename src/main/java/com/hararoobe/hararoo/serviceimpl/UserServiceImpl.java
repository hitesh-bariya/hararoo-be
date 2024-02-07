package com.hararoobe.hararoo.serviceimpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.hararoobe.hararoo.service.UserService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private OtpDataRepository otpDataRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUserName(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmailId(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		return UserDetailsImpl.build(user);
	}

	@PostConstruct
	public void createAdminUser() {
		List<Role> roleList = roleRepository.findAll();
		if (roleList.isEmpty()) {
			Role guest = new Role();
			guest.setId(1L);
			guest.setName(ERole.ROLE_ADMIN);
			roleRepository.save(guest);
			Role user = new Role();
			user.setId(2l);
			user.setName(ERole.ROLE_USER);
			roleRepository.save(user);
			log.info("Role created");
		} else {
			log.info("Role already created");
		}
	}

	@Override
	public ResponseVO<String> signin(LoginRequest loginRequest) {
		ResponseVO<String> response = new ResponseVO<String>();
		try {
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
		} catch (Exception e) {
			log.error("General Errorn in registeredUser for :: ", e.getMessage());
		}
		return response;
	}

	@Override
	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
		try {
			if (userRepository.existsByEmailId(signUpRequest.getEmailId())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
			}
			User user = new User(signUpRequest.getUserName(), signUpRequest.getEmailId(),
					signUpRequest.getContactNumber(), CommonUtils.encode(signUpRequest.getPassword()));
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
		} catch (Exception e) {
			log.error("General Errorn in registerUser for :: ", e.getMessage());
		}
		return null;
	}

	@Override
	public ResponseVO<String> generateEmailVerifyOtp(LoginRequest loginRequest) {
		ResponseVO<String> response = new ResponseVO<String>();
		try {
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

			/*SimpleMailMessage message = new SimpleMailMessage();
			message.setTo("hitubariya310@gmail.com");
			message.setSubject("TEST");
			message.setText("ONE TIME PASSWORD :: "+randomNumber);

			javaMailSender.send(message);*/

			response.setBody(String.valueOf(randomNumber));
		} catch (Exception e) {
			log.error("General Error in generateEmailVerifyOtp for :: ", e.getMessage());
		}
		return response;
	}

	@Override
	public ResponseVO<String> verifyOtp(String emailId, Long otp) {
		ResponseVO<String> response = new ResponseVO<String>();
		try {
			boolean checkOtp = otpDataRepository.existsByEmailIdAndOtpCode(emailId, otp);
			if (checkOtp) {
				Optional<User> userData = userRepository.findByEmailId(emailId);
				if (userData.isPresent()) {
					User user = userData.get();
					user.setEmailVerified(true);
					userRepository.save(user);
				} else {
					response.setStatus(200);
					response.setMessage("EmailId not found");
				}
			} else {
				response.setStatus(200);
				response.setMessage("Invalid OTP");
			}
		} catch (Exception e) {
			log.error("General Errorn in verifyOtp for :: ", e.getMessage());
		}
		return response;
	}
}
