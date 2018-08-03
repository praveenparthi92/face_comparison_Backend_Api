package com.FaceTool.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.FaceTool.Model.FaceIdWithFbIdModel;
import com.FaceTool.Repo.FaceIdWithFbIdRepo;
import com.FaceTool.Service.FaceService;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

@Service
public class FaceServiceimpl implements FaceService {

	@Autowired
	FaceIdWithFbIdRepo fbmodel;

	@Override
	public ArrayList<String> compareFace(MultipartFile photo1, MultipartFile photo2, String collectionid,
			String bucketname) {
		// ## this method for get unmatched face id list
		ArrayList<String> message = new ArrayList<String>();
		try {

			final String collectionId = collectionid;
			final String bucket = bucketname;
			String photoone = photo1.getOriginalFilename();
			String phototwo = photo2.getOriginalFilename();
			ArrayList<FaceMatch> uploadedface = new ArrayList<FaceMatch>();
			ArrayList<FaceMatch> unmatchedface = new ArrayList<FaceMatch>();
			ArrayList<FaceMatch> matchedface = new ArrayList<FaceMatch>();
			ArrayList<String> photos = new ArrayList<String>();
			photos.add(photoone);
			photos.add(phototwo);
			AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2")
					.build();
			for (int i = 0; i < photos.size(); i++) {

				ObjectMapper objectMapper = new ObjectMapper();
				Image imageadd = new Image().withS3Object(new S3Object().withBucket(bucket).withName(photos.get(i)));

				SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
						.withCollectionId(collectionId).withImage(imageadd).withFaceMatchThreshold(10F).withMaxFaces(2);

				SearchFacesByImageResult searchFacesByImageResult = rekognitionClient
						.searchFacesByImage(searchFacesByImageRequest);

				List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();

				for (FaceMatch face : faceImageMatches) {
					System.out.println("obj" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
					System.out.println("faceid" + face.getFace().getFaceId());
					System.out.println("FACEEEEEEEE" + face.getFace());

					uploadedface.add(face);
				}

			}

			for (FaceMatch face : uploadedface) {
				int count = 0;
				for (FaceMatch faceone : uploadedface) {

					if (face.getFace().getFaceId().equalsIgnoreCase(faceone.getFace().getFaceId())) {
						count++;
					}

				}

				if (count == 2) {
					matchedface.add(face);
				} else {
					unmatchedface.add(face);
				}
			}

			for (FaceMatch faceone : unmatchedface) {
				for (FaceMatch facesend : unmatchedface) {
					if (faceone.getFace().getFaceId() != facesend.getFace().getFaceId()) {
						// FaceIdWithFbIdModel
						// matchOne=fbmodel.findByFaceId(faceone.getFace().getFaceId());
						FaceIdWithFbIdModel matchTwo = fbmodel.findByFaceId(facesend.getFace().getFaceId());
						String name1 = faceone.getFace().getExternalImageId().substring(0,
								faceone.getFace().getExternalImageId().lastIndexOf("."));
						String name2 = facesend.getFace().getExternalImageId().substring(0,
								facesend.getFace().getExternalImageId().lastIndexOf("."));

						System.out.println("HI" + " " + name1 + " " + "your suggestions friend  is " + name2 + " "
								+ "and is FacebookId=" + matchTwo.getFaceBookId());

						String msg = "HI" + " " + name1 + " " + "your suggestions friend  is " + name2 + " "
								+ "and is FacebookId=" + matchTwo.getFaceBookId();

						message.add(msg);
					}
				}
			}

			return message;
		} catch (Exception e) {
			ArrayList<String> issue = null;
			e.printStackTrace();
			return issue;
		}

	}

	@Override
	public Boolean createcollections(String collectionName) {
		// ## this method for create collections
		try {
			AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2")
					.build();
			CreateCollectionRequest requestval = new CreateCollectionRequest().withCollectionId(collectionName);
			CreateCollectionResult response = rekognitionClient.createCollection(requestval);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean addphotostocollections(MultipartFile photo, String collectionid, String bucketname) {
		try {
			// ## this method add photos to created collections
			final String collectionId = "FaceCollections";
			final String bucket = "facedetecttool";
			AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2")
					.build();
			Image image = new Image()
					.withS3Object(new S3Object().withBucket(bucket).withName(photo.getOriginalFilename()));

			SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
					.withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(70F).withMaxFaces(2);

			SearchFacesByImageResult searchFacesByImageResult = rekognitionClient
					.searchFacesByImage(searchFacesByImageRequest);

			List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();

			if (faceImageMatches.size() > 0) {
				return false;
			}

			IndexFacesRequest indexFacesRequest = new IndexFacesRequest().withImage(image)
					.withCollectionId(collectionId).withExternalImageId(photo.getOriginalFilename())
					.withDetectionAttributes("ALL");

			IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);
			String faceid;
			List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
			for (FaceRecord faceRecord : faceRecords) {
				if (faceRecord.getFace().getFaceId() != null) {
					faceid = faceRecord.getFace().getFaceId();
					Random rand = new Random();
					int n = rand.nextInt(50) + 1;
					FaceIdWithFbIdModel fb = new FaceIdWithFbIdModel();
					fb.setFaceBookId(photo.getOriginalFilename() + String.valueOf(n));
					fb.setFaceId(faceid);
					fbmodel.save(fb);
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
