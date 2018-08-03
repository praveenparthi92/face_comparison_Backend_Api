package com.FaceTool.Controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FaceTool.Dto.FaceDto;
import com.FaceTool.Dto.StatusResponseDTO;
import com.FaceTool.Service.FaceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;

@CrossOrigin
@RestController
public class FaceComparisionController {

	@Autowired
	FaceService service;

	@Autowired
	private Environment env;

	@CrossOrigin
	@RequestMapping(value = "/FaceComparision", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
	public @ResponseBody ResponseEntity<String> compareface(@RequestParam(value = "credentials") String buket,
			@RequestParam(value = "fileone") MultipartFile targetPhoto,
			@RequestParam(value = "filetwo") MultipartFile sourcePhoto) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		System.out.println("enter");
		ObjectMapper mapper = new ObjectMapper();
		FaceDto face = new FaceDto();
		try {
			face = mapper.readValue(buket, FaceDto.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			ArrayList<String> Result = null;
			Result = service.compareFace(targetPhoto, sourcePhoto, face.getCollectionname(), face.getBucketname());
			if (Result.size() != 0) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("Face"));
				statusResponseDTO.setResult(Result);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("faceissue"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("serverproblem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		}

	}

	@CrossOrigin
	@RequestMapping(value = "/AddComparision", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
	public @ResponseBody ResponseEntity<String> AddfaceToCollection(@RequestParam(value = "credentials") String buket,
			@RequestParam(value = "fileone") MultipartFile targetPhoto) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		ObjectMapper mapper = new ObjectMapper();
		FaceDto face = new FaceDto();
		try {
			face = mapper.readValue(buket, FaceDto.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			boolean result = false;
			result = service.addphotostocollections(targetPhoto, face.getCollectionname(), face.getBucketname());
			if (result) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("successphoto"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("failurephoto"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("serverproblem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		}

	}

}
