package com.FaceTool.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FaceTool.Model.FaceIdWithFbIdModel;

public interface FaceIdWithFbIdRepo extends JpaRepository<FaceIdWithFbIdModel, Integer> {
	
	public FaceIdWithFbIdModel findByFaceId(String faceid);

}
