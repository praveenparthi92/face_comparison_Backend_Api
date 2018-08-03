package com.FaceTool.Controller;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


public class FaceId {
    public static final String collectionId = "MyCollectionfaceTool";
    public static final String bucket = "facedetecttool";
    public static final String photo = "vijayajith.jpg";
      
    public static void main(String[] args) throws Exception {

    	AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();
        
      ObjectMapper objectMapper = new ObjectMapper();
      
       // Get an image object from S3 bucket.
      Image image=new Image()
              .withS3Object(new S3Object()
                      .withBucket(bucket)
                      .withName(photo));
//      
//      IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
//              .withImage(image)
//              .withCollectionId(collectionId)
//              .withExternalImageId(photo)
//              .withDetectionAttributes("ALL");
//      
//      IndexFacesResult indexFacesResult=rekognitionClient.indexFaces(indexFacesRequest);
      
      // Search collection for faces similar to the largest face in the image.
      
      
      SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
              .withCollectionId(collectionId)
              .withImage(image)
              .withFaceMatchThreshold(70F)
              .withMaxFaces(2);
      System.out.println("searchFacesByImageRequest"+searchFacesByImageRequest);
           
       SearchFacesByImageResult searchFacesByImageResult = 
               rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
      
      
      List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();
      System.out.println("faceImageMatches" + faceImageMatches.size());
      for (FaceMatch face: faceImageMatches) {
    	  System.out.println("Faces matching" + face.getFace());
          System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                  .writeValueAsString(face));
         System.out.println("faceid"+face.getFace().getFaceId());
         System.out.println("FACEEEEEEEE"+face.getFace());
      }
      
      

//      DetectFacesRequest request = new DetectFacesRequest()
//         .withImage(new Image()
//            .withS3Object(new S3Object()
//               .withName(photo)
//               .withBucket(bucket)))
//         .withAttributes(Attribute.ALL);
//      // Replace Attribute.ALL with Attribute.DEFAULT to get default values.
//
//      AmazonRekognition rekognitionClientone = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();
//         DetectFacesResult result = rekognitionClientone.detectFaces(request);
//         List < FaceDetail > faceDetails = result.getFaceDetails();
//         
//         for (FaceDetail face: faceDetails) {
//             if (request.getAttributes().contains("ALL")) {
//                AgeRange ageRange = face.getAgeRange();
//                System.out.println("The detected face is estimated to be between "
//                   + ageRange.getLow().toString() + " and " + ageRange.getHigh().toString()
//                   + " years old.");
//                System.out.println("Here's the complete set of attributes:");
//             } else { // non-default attributes have null values.
//                System.out.println("Here's the default set of attributes:");
//             }
//
//             ObjectMapper objectMapperone = new ObjectMapper();
//             System.out.println(objectMapperone.writerWithDefaultPrettyPrinter().writeValueAsString(face));
//             System.out.println("faceiDDDDDDDDDDDD"+face);
//          }
    }
    

       
      
}



