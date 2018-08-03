package com.FaceTool.Controller;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.DeleteFacesResult;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;

import java.util.ArrayList;
import java.util.List;


public class DeleteFace {
   public static final String collectionId = "FaceCollections";
	

   public static void main(String[] args) throws Exception {
      
	   AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();
	    ArrayList<String> uploadedface = new ArrayList<String>();
	   ListFacesResult listFacesResult = null;

     String paginationToken = null;
     do {
        if (listFacesResult != null) {
           paginationToken = listFacesResult.getNextToken();
        }
        
        ListFacesRequest listFacesRequest = new ListFacesRequest()
                .withCollectionId(collectionId)
                .withMaxResults(1)
                .withNextToken(paginationToken);
       
        listFacesResult =  rekognitionClient.listFaces(listFacesRequest);
        List < Face > faces = listFacesResult.getFaces();
        
        int i=0;
        for (Face face: faces) {
       	
       	System.out.println("faceidddddddddd"+face.getFaceId());
           System.out.println("faceeeeeeeee"+face);
       		uploadedface.add(face.getFaceId());
          
        }
	   
      for(String faceid:uploadedface){
      DeleteFacesRequest deleteFacesRequest = new DeleteFacesRequest()
              .withCollectionId(collectionId)
              .withFaceIds(faceid);
     
      DeleteFacesResult deleteFacesResult=rekognitionClient.deleteFaces(deleteFacesRequest);
      
      List < String > faceRecords = deleteFacesResult.getDeletedFaces();
      System.out.println(Integer.toString(faceRecords.size()) + " face(s) deleted:");
      for (String face: faceRecords) {
         System.out.println("FaceID: " + face);
      }
      }
      
     
   }while (listFacesResult != null && listFacesResult.getNextToken() !=
	         null);
   }

}