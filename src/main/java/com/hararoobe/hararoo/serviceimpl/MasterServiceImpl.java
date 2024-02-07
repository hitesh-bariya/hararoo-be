package com.hararoobe.hararoo.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hararoobe.hararoo.entity.JobCategory;
import com.hararoobe.hararoo.model.JobCategoryDTO;
import com.hararoobe.hararoo.repository.JobCategoryRepository;
import com.hararoobe.hararoo.service.MasterService;

@Service
public class MasterServiceImpl implements MasterService{

	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private JobCategoryRepository jobCategoryRepository;
	
	@Override
	public JobCategoryDTO savaJobCategory(JobCategoryDTO jobCategoryDTO) {
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		JobCategory JobCategory = mapper.convertValue(jobCategoryDTO, JobCategory.class);
		JobCategory = jobCategoryRepository.save(JobCategory);
		return mapper.convertValue(JobCategory, JobCategoryDTO.class);
	}

	@Override
	public List<JobCategoryDTO> getJobCategoryList() {
		List<JobCategory> jobCategoryList=jobCategoryRepository.findAll();
		List<JobCategoryDTO> data=new ArrayList<>();
		for(JobCategory jobCategory:jobCategoryList) {
			JobCategoryDTO jobCategoryDTO=mapper.convertValue(jobCategory, JobCategoryDTO.class);
			data.add(jobCategoryDTO);
		}
		return data;
	}

	
}
