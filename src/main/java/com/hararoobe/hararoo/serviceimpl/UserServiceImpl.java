package com.hararoobe.hararoo.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hararoobe.hararoo.entity.Role;
import com.hararoobe.hararoo.entity.User;
import com.hararoobe.hararoo.enums.ERole;
import com.hararoobe.hararoo.repository.RoleRepository;
import com.hararoobe.hararoo.repository.UserRepository;
import com.hararoobe.hararoo.service.UserService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	 @Autowired
	  UserRepository userRepository;
	 
	 @Autowired
	 RoleRepository roleRepository;

	  @Override
	  @Transactional
	  public UserDetails loadUserByUserName(String username) throws UsernameNotFoundException {
	    User user = userRepository.findByEmailId(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

	    return UserDetailsImpl.build(user);
	  }
	  
	  @PostConstruct
	    public void createAdminUser(){
	       List<Role> roleList=roleRepository.findAll();
	       if(roleList.isEmpty()) {
	    	   Role guest=new Role();
	    	   guest.setId(1L);
	    	   guest.setName(ERole.ROLE_ADMIN);
	    	   roleRepository.save(guest);
	    	   Role user=new Role();
	    	   user.setId(2l);
	    	   user.setName(ERole.ROLE_USER);
	    	   roleRepository.save(user);
	    	   log.info("Role created");
	       }else {
	    	   log.info("Role already created");
	       }
	    }
}
