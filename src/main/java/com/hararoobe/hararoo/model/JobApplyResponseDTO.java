package com.hararoobe.hararoo.model;

import com.hararoobe.hararoo.entity.DocumentData;
import com.hararoobe.hararoo.entity.JobData;
import com.hararoobe.hararoo.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplyResponseDTO {

	private User user;
	
	private JobData jobData;

	private DocumentData document;

}
