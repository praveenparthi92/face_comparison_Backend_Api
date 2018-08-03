package com.FaceTool.Controller;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;



public class AllFaces {
    public static final String collectionId = "FaceCollections";

   public static void main(String[] args) throws Exception {
      
	   AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();

      ObjectMapper objectMapper = new ObjectMapper();

      ListFacesResult listFacesResult = null;
//      System.out.println("Faces in collection " + collectionId);

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
        	
        	
//            System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
//               .writeValueAsString(face));
            System.out.println("faceidddddddddd"+face.getFaceId());
            System.out.println("faceeeeeeeee"+face);
           
         }
      } while (listFacesResult != null && listFacesResult.getNextToken() !=
         null);
   }

}