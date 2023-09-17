package com.hararoobe.hararoo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	 @GetMapping("/user")
	public String test() {
		return "Successed";
	}
	

}
