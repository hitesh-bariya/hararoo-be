package com.hararoobe.hararoo.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hararoobe.hararoo.model.ResponseVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
	
	@Autowired
    private ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		try {
			
			logger.error("Unauthorized error: {}", authException.getMessage());
			//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
			sendErrorResponse(response);
			//throw new Test("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private void sendErrorResponse(HttpServletResponse response)
			throws IOException {
		ResponseVO<Object> responseVO = ResponseVO.builder().status(401).errorMsg("Unthentication error").build();
		SecurityContextHolder.clearContext();
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(401);
		response.getWriter().write(objectMapper.writeValueAsString(responseVO));
		response.getWriter().flush();
		response.getWriter().close();
		
		

	}
}
