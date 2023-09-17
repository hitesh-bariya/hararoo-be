package com.hararoobe.hararoo.model;

import com.hararoobe.hararoo.entity.DocumentData;
import com.hararoobe.hararoo.entity.JobData;
import com.hararoobe.hararoo.entity.User;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JobApplyResponseDTO {

	private User user;
	
	private JobData jobData;

	private DocumentData document;

}
