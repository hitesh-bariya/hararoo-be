package com.hararoobe.hararoo.serviceimpl;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hararoobe.hararoo.entity.DocumentData;
import com.hararoobe.hararoo.entity.JobData;
import com.hararoobe.hararoo.entity.Role;
import com.hararoobe.hararoo.entity.User;
import com.hararoobe.hararoo.enums.ERole;
import com.hararoobe.hararoo.model.JobApplyDTO;
import com.hararoobe.hararoo.model.JobApplyResponseDTO;
import com.hararoobe.hararoo.model.JobDataDTO;
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
	public JobDataDTO updateJobStatus(Long jobId, boolean jobStatus) {

		return null;
	}

	@Override
	public List<JobDataDTO> getAllJobData(Integer pageNo, Integer pageSize, String sortBy) {
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
		return responseData;
	}

	@Override
	public List<JobDataDTO> getAllActiveJobData(Integer pageNo, Integer pageSize, String sortBy) {
		List<JobDataDTO> responseData = new ArrayList<>();
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		List<JobData> jobData = jobDataRepository.findByStatus(true, paging);
		for (JobData data : jobData) {
			JobDataDTO jobDataDTO = mapper.convertValue(data, JobDataDTO.class);
			responseData.add(jobDataDTO);
		}
		return responseData;
	}

	@Override
	public JobApplyResponseDTO saveJobApplyData(JobApplyDTO jobApplyDTO,MultipartFile resumeFile) {
		Set<Role> roles = new HashSet<>();
		Optional<Role> role = roleRepository.findByName(ERole.ROLE_USER);
		roles.add(role.get());
		JobData jobData = jobDataRepository.findById(jobApplyDTO.getJobId()).get();
		User user = User.builder().userName(jobApplyDTO.getUserName()).emailId(jobApplyDTO.getEmailId())
				.contactNumber(jobApplyDTO.getContactNumber()).password(encoder.encode(jobApplyDTO.getPassword()))
				.roles(roles).jobData(jobData).build();
		user = userRepository.save(user);

		DocumentData documentData = DocumentData.builder().documentUrl("").user(user).build();
		documentData = documentDataRepository.save(documentData);

		JobApplyResponseDTO jobApplyResponseDTO = JobApplyResponseDTO.builder().user(user).document(documentData)
				.jobData(jobData).build();

		return jobApplyResponseDTO;
	}

}
