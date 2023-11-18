package com.hararoobe.hararoo.serviceimpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hararoobe.hararoo.common.CommonUtils;
import com.hararoobe.hararoo.entity.DocumentData;
import com.hararoobe.hararoo.entity.JobData;
import com.hararoobe.hararoo.entity.Role;
import com.hararoobe.hararoo.entity.User;
import com.hararoobe.hararoo.enums.ERole;
import com.hararoobe.hararoo.model.JobApplyDTO;
import com.hararoobe.hararoo.model.JobApplyResponseDTO;
import com.hararoobe.hararoo.model.JobDataDTO;
import com.hararoobe.hararoo.model.ResponseVO;
import com.hararoobe.hararoo.repository.DocumentDataRepository;
import com.hararoobe.hararoo.repository.JobDataRepository;
import com.hararoobe.hararoo.repository.RoleRepository;
import com.hararoobe.hararoo.repository.UserRepository;
import com.hararoobe.hararoo.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobServiceImpl implements JobService {

	@Autowired
	private JobDataRepository jobDataRepository;

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DocumentDataRepository documentDataRepository;

	private static final String UPLOADED_FOLDER = "src/main/resources/uploads/";

	@Override
	public JobDataDTO saveJob(JobDataDTO jobRequestDTO) {
		try {
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JobData jobData = mapper.convertValue(jobRequestDTO, JobData.class);
			jobData = jobDataRepository.save(jobData);
			return mapper.convertValue(jobData, JobDataDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in saveJob for message :: {}", e.getMessage());
		}
		return null;
	}

	@Override
	public JobDataDTO updateJob(JobDataDTO jobRequestDTO) {
		try {
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			JobData jobData = mapper.convertValue(jobRequestDTO, JobData.class);
			jobData = jobDataRepository.save(jobData);
			return mapper.convertValue(jobData, JobDataDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in saveJob for message :: {}", e.getMessage());
		}
		return null;
	}

	@Override
	public ResponseVO<JobDataDTO> updateJobStatus(Long jobId, boolean jobStatus) {
		ResponseVO<JobDataDTO> response = new ResponseVO<JobDataDTO>();
		Optional<JobData> jobData=jobDataRepository.findById(jobId);
		if(jobData.isPresent()) {
			JobData job=jobData.get();
			job.setStatus(true);
			jobDataRepository.save(job);
			response.setStatus(200);
			response.setMessage("Job status updated");
		}else {
			response.setStatus(200);
			response.setMessage("Job not found.");
		}
		return response;
	}

	@Override
	public ResponseVO<List<JobDataDTO>> getAllJobData(Integer pageNo, Integer pageSize, String sortBy) {
		ResponseVO<List<JobDataDTO>> response=new ResponseVO<List<JobDataDTO>>();
		List<JobDataDTO> responseData = new ArrayList<>();
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<JobData> pagedResult = jobDataRepository.findAll(paging);
		if (pagedResult.hasContent()) {
			List<JobData> jobDt = pagedResult.getContent();
			for (JobData data : jobDt) {
				JobDataDTO jobDataDTO = mapper.convertValue(data, JobDataDTO.class);
				responseData.add(jobDataDTO);
			}
		}
		response.setStatus(200);
		response.setBody(responseData);
		return response;
	}

	@Override
	public ResponseVO<List<JobDataDTO>> getAllActiveJobData(Integer pageNo, Integer pageSize, String sortBy) {
		ResponseVO<List<JobDataDTO>> response=new ResponseVO<List<JobDataDTO>>();
		List<JobDataDTO> responseData = new ArrayList<>();
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		List<JobData> jobData = jobDataRepository.findByStatus(true, paging);
		for (JobData data : jobData) {
			JobDataDTO jobDataDTO = mapper.convertValue(data, JobDataDTO.class);
			responseData.add(jobDataDTO);
		}
		response.setStatus(200);
		response.setBody(responseData);
		return response;
	}

	@Override
	public JobApplyResponseDTO saveJobApplyData(JobApplyDTO jobApplyDTO, MultipartFile resumeFile) {
		JobApplyResponseDTO jobApplyResponseDTO = new JobApplyResponseDTO();
		try {
			Set<Role> roles = new HashSet<>();
			Optional<Role> role = roleRepository.findByName(ERole.ROLE_USER);
			roles.add(role.get());
			JobData jobData = jobDataRepository.findById(jobApplyDTO.getJobId()).get();
			User user = User.builder().userName(jobApplyDTO.getUserName()).emailId(jobApplyDTO.getEmailId())
					.contactNumber(jobApplyDTO.getContactNumber())
					.password(CommonUtils.encode(jobApplyDTO.getPassword())).roles(roles).jobData(jobData).build();
			user = userRepository.save(user);
			byte[] bytes = resumeFile.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER +user.getEmailId()+"_"+jobData.getJobId()+"_"+resumeFile.getOriginalFilename());
			Files.write(path, bytes);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/files/download/")
					.path(user.getEmailId()+"_"+jobData.getJobId()+"_"+resumeFile.getOriginalFilename()).toUriString();
			DocumentData documentData = DocumentData.builder().documentUrl(fileDownloadUri).user(user).build();
			documentData = documentDataRepository.save(documentData);
			jobApplyResponseDTO = JobApplyResponseDTO.builder().user(user).document(documentData).jobData(jobData)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in saveJobApplyData for message :: {}", e.getMessage());
		}
		return jobApplyResponseDTO;
	}

}
