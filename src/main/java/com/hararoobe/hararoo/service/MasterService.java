package com.hararoobe.hararoo.service;

import java.util.List;

import com.hararoobe.hararoo.model.JobCategoryDTO;

public interface MasterService {

	JobCategoryDTO savaJobCategory(JobCategoryDTO jobCategoryDTO);
	
	List<JobCategoryDTO> getJobCategoryList();
	
}
