package com.FaceTool.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.FaceTool.Dto.FaceDto;


public interface FaceService{
	public ArrayList<String> compareFace(MultipartFile photo1,MultipartFile photo2,String collectionid,String bucketname);
	public Boolean createcollections(String collectionName);
	public Boolean addphotostocollections(MultipartFile photo,String collectionid,String bucketname);
	

}
