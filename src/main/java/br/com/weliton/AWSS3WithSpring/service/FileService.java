package br.com.weliton.AWSS3WithSpring.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;

@Service
public class FileService {

	@Autowired
	private AmazonS3 s3client;
	
	@Value("${aws.s3.bucket}")
	private String bucket;
	
	public ResponseEntity<String> uploadFile(MultipartFile file) {
		try {
			File localFile = new File(file.getOriginalFilename());
			file.transferTo(localFile);
			String path = file.getOriginalFilename();
			s3client.putObject(bucket, path, localFile);
			return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
		} catch (AmazonServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Problem in Amazon Acess" + " - " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
