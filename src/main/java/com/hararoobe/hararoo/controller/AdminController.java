package com.hararoobe.hararoo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hararoobe.hararoo.model.JobDataDTO;
import com.hararoobe.hararoo.model.ResponseVO;
import com.hararoobe.hararoo.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private JobService jobService;

	@PostMapping("/job-post")
	public ResponseVO<?> jobPost(@RequestBody JobDataDTO jobRequestDTO) {
		log.info("Calling Contoller for jobPost mrthod");
		
		return ResponseVO.builder().status(201).body(jobService.saveJob(jobRequestDTO)).build();
	}

	@PutMapping("/job-post-update")
	public ResponseVO<?> jobPostUpdate(@RequestBody JobDataDTO jobRequestDTO) {
		log.info("Calling Contoller for jobPostUpdate mrthod");
		
		return ResponseVO.builder().status(201).body(jobService.saveJob(jobRequestDTO)).build();
	}

	@GetMapping("/update-job-status")
	public ResponseVO<JobDataDTO> updateJobStatus(@RequestParam("status") Long jobId,
			@RequestParam("status") boolean status) {
		log.info("Calling Contoller for updateJobStatus method");
		return jobService.updateJobStatus(jobId, status);
		
	}

	@GetMapping("/all-jobs")
	public ResponseVO<List<JobDataDTO>> getAllJobs(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "jobId") String sortBy) {
		log.info("Calling Contoller for getAllJobs mrthod");
		
		return jobService.getAllJobData(pageNo, pageSize, sortBy);
	}

	@GetMapping("/all-active-jobs")
	public ResponseVO<?> getAllActiveJobs(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "jobId") String sortBy) {
		log.info("Calling Contoller for getAllActivemJobs mrthod");
		return ResponseVO.builder().status(201).body(jobService.getAllActiveJobData(pageNo, pageSize, sortBy)).build();
	}
}
