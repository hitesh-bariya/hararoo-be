package com.hararoobe.hararoo.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.hararoobe.hararoo.entity.OtpData;

public interface OtpDataRepository extends JpaRepository<OtpData, Long>{

	boolean existsByEmailIdAndOtpCode(String emailId,Long otpCode);
	
	@Modifying
	@Transactional
    @Query("DELETE FROM OtpData m WHERE m.createdAt < :cutoffTime")
    int deleteOlderOtp(Date cutoffTime);
}
