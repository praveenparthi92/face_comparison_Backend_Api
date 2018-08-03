package com.FaceTool.Controller;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;

import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.util.IOUtils;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FaceTool {

   public static void main(String[] args) throws Exception{
	   
	  
	  
	   
       Float similarityThreshold = 50F;
       final String collectionId = "MyCollectionfaceTool";
       final String bucket = "facedetecttool";
       final String photo = "ajith_vijay.jpg";
       final String photo2 = "collections.jpg";
       String sourceImage = "E://face detect/ajith_vijay.jpg";
       String targetImage = "E://face detect/collections.jpg";
       ByteBuffer sourceImageBytes=null;
       ByteBuffer targetImageBytes=null;

       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();
//       CreateCollectionRequest requestval = new CreateCollectionRequest().withCollectionId("MyCollectionfaceTool");
//	   CreateCollectionResult response = rekognitionClient.createCollection(requestval);
	   //System.out.println("response"+response);

       //Load source and target images and create input parameters
       try (InputStream inputStream = new FileInputStream(new File(sourceImage))) {
          sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load source image " + sourceImage);
           System.exit(1);
       }
       try (InputStream inputStream = new FileInputStream(new File(targetImage))) {
           targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load target images: " + targetImage);
           System.exit(1);
       }

       Image source=new Image()
            .withBytes(sourceImageBytes);
       Image target=new Image()
            .withBytes(targetImageBytes);

       CompareFacesRequest request = new CompareFacesRequest()
               .withSourceImage(source)
               .withTargetImage(target)
               .withSimilarityThreshold(similarityThreshold);

       // Call operation
       CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);


       // Display results
       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
       for (CompareFacesMatch match: faceDetails){
         ComparedFace face= match.getFace();
         BoundingBox position = face.getBoundingBox();
         System.out.println("Face at " + position.getLeft().toString()
               + " " + position.getTop()
               + " matches with " + face.getConfidence().toString()
               + "% confidence.");
        
         System.out.println("faceee????? match"+match);
         System.out.println("getface"+match.getFace());

       }
       
       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();
       
       for (ComparedFace match: uncompared){
          
    	   System.out.println("unmatchimages%%%%%%%%%%%"+match);
           System.out.println("UnMatcHHHHHHHHHH????? match"+match);

         }

       System.out.println("There was unmatch face count " + uncompared.size()
            + " face(s) that did not match");
       
       List<CompareFacesMatch> compared = compareFacesResult.getFaceMatches();

       System.out.println("There was match face count " + compared.size()
            + " face(s) are match");
       System.out.println("Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection());
       System.out.println("target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection());
       
       
       
       
//      
//       AmazonRekognition rekognitionClientvalue = AmazonRekognitionClientBuilder.standard().withRegion("us-east-2").build();
//       ObjectMapper objectMapper = new ObjectMapper();
//       
//        // Get an image object from S3 bucket.
//       Image image=new Image()
//               .withS3Object(new S3Object()
//                       .withBucket(bucket)
//                       .withName(photo));
//              
//              
//      
//             System.out.println("images"+image);
//             
//       // Search collection for faces similar to the largest face in the image.
//       SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
//               .withCollectionId(collectionId)
//               .withImage(image)
//               .withFaceMatchThreshold(70F)
//               .withMaxFaces(2);
//              System.out.println("beforeeeeee"+searchFacesByImageRequest);
//            
//        SearchFacesByImageResult searchFacesByImageResult = 
//        		rekognitionClientvalue.searchFacesByImage(searchFacesByImageRequest);
//
//        System.out.println("Faces matching largest face in image from" + searchFacesByImageResult);
//       List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();
//       System.out.println("faceImageMatches" + faceImageMatches);
//       for (FaceMatch face: faceImageMatches) {
//           System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
//                   .writeValueAsString(face));
//          System.out.println("faceid"+face.getFace().getFaceId());
//       }
    }
   
}