package com.FaceTool.Dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


public class StatusResponseDTO {
	public String status;
	public String message;
	public String bucketname;
	public String collectionname;
	
	private List<String> Result;
	
	
	
	
	public List<String> getResult() {
		return Result;
	}
	public void setResult(List<String> result) {
		Result = result;
	}
	public String getBucketname() {
		return bucketname;
	}
	public void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}
	public String getCollectionname() {
		return collectionname;
	}
	public void setCollectionname(String collectionname) {
		this.collectionname = collectionname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
