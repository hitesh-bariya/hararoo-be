package com.hararoobe.hararoo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hararoobe.hararoo.model.JobApplyDTO;
import com.hararoobe.hararoo.model.JobApplyResponseDTO;
import com.hararoobe.hararoo.model.JobDataDTO;
import com.hararoobe.hararoo.model.ResponseVO;


public interface JobService {

	public JobDataDTO saveJob(JobDataDTO jobRequestDTO);
	
	public JobDataDTO updateJob(JobDataDTO jobRequestDTO);
	
	public ResponseVO<JobDataDTO> updateJobStatus(Long jobId,boolean jobStatus);
	
	ResponseVO<List<JobDataDTO>> getAllJobData(Integer pageNo,Integer pageSize,String sortBy);
	
	ResponseVO<List<JobDataDTO>> getAllActiveJobData(Integer pageNo,Integer pageSize,String sortBy);
	
	public JobApplyResponseDTO saveJobApplyData(JobApplyDTO jobApplyDTO,MultipartFile resumeFile);
}
