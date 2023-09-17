package com.hararoobe.hararoo.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {

	    @JsonProperty("status")
	    private Integer status;

	    @JsonProperty("message")
	    private String message;

	    @JsonProperty("errorCode")
	    private Long errorCode;

	    @JsonProperty("errorMessage")
	    private String errorMsg;
	    
	    @JsonProperty("body")
	    private T body;
}
