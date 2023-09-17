package com.hararoobe.hararoo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hararoobe.hararoo.model.JobApplyDTO;
import com.hararoobe.hararoo.model.JobApplyResponseDTO;
import com.hararoobe.hararoo.model.JobDataDTO;


public interface JobService {

	public JobDataDTO saveJob(JobDataDTO jobRequestDTO);
	
	public JobDataDTO updateJob(JobDataDTO jobRequestDTO);
	
	public JobDataDTO updateJobStatus(Long jobId,boolean jobStatus);
	
	List<JobDataDTO> getAllJobData(Integer pageNo,Integer pageSize,String sortBy);
	
	List<JobDataDTO> getAllActiveJobData(Integer pageNo,Integer pageSize,String sortBy);
	
	public JobApplyResponseDTO saveJobApplyData(JobApplyDTO jobApplyDTO,MultipartFile resumeFile);
}
