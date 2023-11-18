package com.hararoobe.hararoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hararoobe.hararoo.entity.OtpData;

public interface OtpDataRepository extends JpaRepository<OtpData, Long>{

	boolean existsByEmailIdAndOtpCode(String emailId,Long otpCode);
}
