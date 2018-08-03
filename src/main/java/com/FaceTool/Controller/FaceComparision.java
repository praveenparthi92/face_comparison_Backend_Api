package com.FaceTool.Controller;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FaceComparision {
	 public static void main(String[] args) throws Exception {
	 Float similarityThreshold = 50F;
     final String collectionId = "FaceCollections";
     final String bucket = "facedetecttool";
     //final String photo = "Madhva.jpg";

//   String sourceImage = "E://face detect/ajith_vijay.jpg";
   String photoone = "vijiayAjith.jpg";
   String phototwo = "Suriya-and-Vijay-Risk.jpg";
   ByteBuffer sourceImageBytes=null;
   ByteBuffer targetImageBytes=null;
   
   ArrayList<FaceMatch> uploadedface=new ArrayList<FaceMatch>();
   ArrayList<FaceMatch> unmatchedface=new ArrayList<FaceMatch>();
   ArrayList<FaceMatch> matchedface=new ArrayList<FaceMatch>();
   
   ArrayList<String> photos=new ArrayList<String>();
   photos.add(photoone);
   photos.add(phototwo);
     AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();
     


  //## this method for create collections 
//     CreateCollectionRequest requestval = new CreateCollectionRequest().withCollectionId("FaceCollections");
//	   CreateCollectionResult response = rekognitionClient.createCollection(requestval);
//	   System.out.println("response"+response);
    
    
     
     
  // ## this method add photos to created collections 
//     Image image=new Image()
//             .withS3Object(new S3Object()
//                     .withBucket(bucket)
//                     .withName(photo));
//     
//     IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
//             .withImage(image)
//             .withCollectionId(collectionId)
//             .withExternalImageId(photo)
//             .withDetectionAttributes("ALL");
//     
//     IndexFacesResult indexFacesResult=rekognitionClient.indexFaces(indexFacesRequest);

     
     for(int i=0;i<photos.size();i++){
     System.out.println("photos.get(i)"+photos.get(i));
     ObjectMapper objectMapper = new ObjectMapper();
    Image imageadd=new Image()
            .withS3Object(new S3Object()
                    .withBucket(bucket)
                    .withName(photos.get(i)));

    SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
            .withCollectionId(collectionId)
            .withImage(imageadd)
            .withFaceMatchThreshold(20F)
            .withMaxFaces(2);
    System.out.println("searchFacesByImageRequest"+searchFacesByImageRequest);
         
     SearchFacesByImageResult searchFacesByImageResult = 
             rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
    
    
    List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();
   
    for (FaceMatch face: faceImageMatches) {
  	  
        System.out.println("obj"+objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(face));
       System.out.println("faceid"+face.getFace().getFaceId());
       System.out.println("FACEEEEEEEE"+face.getFace());
       uploadedface.add(face);
    }     
     
     }
   
     
     for(FaceMatch face: uploadedface){
    	 int count=0;
    	 for(FaceMatch faceone: uploadedface){
    		
    		 if(face.getFace().getFaceId().equalsIgnoreCase(faceone.getFace().getFaceId())){
    			 count++;
    		 }
    		
    	 }
    	
    	 if(count==2){
    		 matchedface.add(face);
    	 }
    	 else{
    		 unmatchedface.add(face);
    	 }
    	 }
    	 
    	for( FaceMatch faceone: unmatchedface){
    		System.out.println("unamatched face id"+faceone.getFace().getFaceId());
    		System.out.println("unamatched face"+faceone.getFace().getExternalImageId());
    	}
    	
    	for( FaceMatch faceone: unmatchedface){
    		for( FaceMatch facesend: unmatchedface){
    			if(faceone.getFace().getFaceId()!=facesend.getFace().getFaceId()){
    			System.out.println("HI"+faceone.getFace().getExternalImageId()+""+facesend.getFace().getExternalImageId()+"is your friend");
    		}
    		}
    	}
    
     }


}
