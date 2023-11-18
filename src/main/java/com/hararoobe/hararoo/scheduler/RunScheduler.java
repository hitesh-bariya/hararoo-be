package com.hararoobe.hararoo.scheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hararoobe.hararoo.common.CommonUtils;
import com.hararoobe.hararoo.repository.OtpDataRepository;

@Component
public class RunScheduler {

	@Autowired
	OtpDataRepository otpDataRepository;

	//@Scheduled(cron = "*/5 * * * * *")
	public void myScheduledTask() {
		LocalDateTime cutoffTime = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
		Date date = CommonUtils.convertLocalDateTimeToDate(cutoffTime);
		int record=otpDataRepository.deleteOlderOtp(date);
		System.out.println("Record Deleted :: "+record);
	}


}
