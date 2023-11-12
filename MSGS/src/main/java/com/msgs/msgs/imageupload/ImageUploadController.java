package com.msgs.msgs.imageupload;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;

import jakarta.servlet.http.HttpSession;


public class ImageUploadController {

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

	@Value("${objectStorage.endPoint}")
	private String endPoint;

	@Value("${cloud.aws.region.static}")
	private String regionName;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public List<String>  uploadFilesSample2(List<Object> imageList, String pathName ,HttpSession session) throws Exception{

		String path = pathName;
		String originalName;
		long size;

		List<String> imageLinkList = new ArrayList<>();




		// S3 client
		AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withRegion(regionName)
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();



		try {



			for (Object image : imageList) {
				byte[] byteArr = Base64.decode(image.toString().substring(image.toString().indexOf(",") + 1));
				UUID uuid = UUID.randomUUID();
				String newFileName = uuid.toString();

				ByteArrayInputStream fileData = new ByteArrayInputStream(byteArr);


				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(byteArr.length);
				metadata.setContentType("image/jpeg");
				metadata.setCacheControl("public, max-age=31536000");


				// 업로드
				s3.putObject(
						new PutObjectRequest(bucketName + path, newFileName, fileData, metadata)
								.withCannedAcl(CannedAccessControlList.PublicRead)
				);

				String imagePath = s3.getUrl(bucketName + path, newFileName).toString(); // 접근가능한 URL 가져오기

//          imageKeyLink.put(newFileName, imagePath);
				//imageKeyLink.put("imgPath", imagePath);
				imageLinkList.add(imagePath);

			}





		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		} catch(SdkClientException e) {
			e.printStackTrace();
		}




		return  imageLinkList;

	}

	public List<HashMap<String, String>>  uploadFilesSample(List<Object> imageList, String pathName ,HttpSession session) throws Exception{

		String path = pathName;
		String originalName;
		long size;
		String bucketName = "msgs-file-server";
		String endPoint = "https://kr.object.ncloudstorage.com";
		String regionName = "kr-standard";
		String accessKey = "6fCMolib7QBe1JKwSafq";
		String secretKey = "miJ3BdZsKPsE3WLliwHPJbJS7qaxby6F6rDiVTJa";

		List<HashMap<String, String>> imageLinkList = new ArrayList<>();
		HashMap<String, String> imageKeyLink = new HashMap<>();


		// S3 client
		AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();



		try {

			int index = 1;

			for (Object image : imageList) {
				byte[] byteArr = Base64.decode(image.toString().substring(image.toString().indexOf(",") + 1));
				UUID uuid = UUID.randomUUID();
				String newFileName = uuid.toString();

				ByteArrayInputStream fileData = new ByteArrayInputStream(byteArr);


				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(byteArr.length);
				metadata.setContentType("image/jpeg");
				metadata.setCacheControl("public, max-age=31536000");


				// 업로드
				s3.putObject(
						new PutObjectRequest(bucketName + path, newFileName, fileData, metadata)
								.withCannedAcl(CannedAccessControlList.PublicRead)
				);

				String imagePath = s3.getUrl(bucketName + path, newFileName).toString(); // 접근가능한 URL 가져오기

				imageKeyLink.put("key" + index, imagePath);
				imageLinkList.add(imageKeyLink);

				index++;
			}

		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		} catch(SdkClientException e) {
			e.printStackTrace();
		}

		return  imageLinkList;
	}


}