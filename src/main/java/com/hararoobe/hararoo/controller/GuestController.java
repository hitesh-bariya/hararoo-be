package com.hararoobe.hararoo.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hararoobe.hararoo.model.JobApplyDTO;
import com.hararoobe.hararoo.model.JobApplyResponseDTO;
import com.hararoobe.hararoo.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/guest")
public class GuestController {

	@Autowired
	private JobService jobService;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@PostMapping("/job-apply")
	public ResponseEntity<JobApplyResponseDTO> jobApply(@RequestParam(value = "jobRequestData", required = true) @Valid String jsondata,
            @RequestParam(value = "files", required = true) @Valid MultipartFile resumeFile) {
		log.info("Calling Contoller for jobPost mrthod");
		try {
			JobApplyDTO jobApplyDTO = objectMapper.readValue(jsondata, JobApplyDTO.class);
			return new ResponseEntity<JobApplyResponseDTO>(jobService.saveJobApplyData(jobApplyDTO,resumeFile), HttpStatus.CREATED);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/job-search")
	public ResponseEntity<JobApplyResponseDTO> jobSearch() {
		log.info("Calling Contoller for jobSearch mrthod");
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
		Path filePath = Paths.get("src/main/resources/uploads/").resolve(fileName);
		try {
			 Resource resource = new UrlResource(filePath.toUri());
	            if (resource.exists()) {
	                return ResponseEntity.ok()
	                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
	                        .body(resource);
	            } else {
	                return ResponseEntity.notFound().build();
	            }
		} catch (Exception e) {
			e.printStackTrace();
            return ResponseEntity.status(500).build();
		}
	}
}
