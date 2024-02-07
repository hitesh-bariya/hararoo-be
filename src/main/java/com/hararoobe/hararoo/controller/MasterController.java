package com.hararoobe.hararoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hararoobe.hararoo.model.JobCategoryDTO;
import com.hararoobe.hararoo.model.ResponseVO;
import com.hararoobe.hararoo.service.MasterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/master")
@CrossOrigin("*")
public class MasterController {

	
	@Autowired
	MasterService masterService;
	
	@PostMapping("/save-job-category")
	public ResponseVO<?> savaJobCategory(@RequestBody JobCategoryDTO jobCategoryDTO) {
		log.info("Calling Contoller for savaJobCategory mrthod");
		return ResponseVO.builder().status(201).body(masterService.savaJobCategory(jobCategoryDTO)).build();
	}
	
	@GetMapping("/get-job-category")
	public ResponseVO<?> getJobCategory() {
		log.info("Calling Contoller for getJobCategory mrthod");
		return ResponseVO.builder().status(201).body(masterService.getJobCategoryList()).build();
	}
}
