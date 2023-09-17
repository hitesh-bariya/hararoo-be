package com.hararoobe.hararoo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hararoobe.hararoo.entity.JobData;

public interface JobDataRepository extends JpaRepository<JobData, Long> {

	List<JobData> findByStatus(boolean status, Pageable paging);

}
