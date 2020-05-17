package br.com.weliton.AWSS3WithSpring.service;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class FileService {

	@Autowired
	private AmazonS3 s3client;
	
	@Value("${aws.s3.bucket}")
	private String bucket;
	
	public ResponseEntity<?> uploadFile(MultipartFile file) {
		try {
			File localFile = new File(file.getOriginalFilename());
			file.transferTo(localFile);
			String path = file.getOriginalFilename();
			s3client.putObject(bucket, path, localFile);
			return ResponseEntity.status(HttpStatus.OK).body(file.getOriginalFilename());
		} catch (AmazonServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Problem in Amazon Access" + " - " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getFile(String fileName) {
		try {
			if (s3client.doesObjectExist(bucket, fileName)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file does not exist");
			} else {
				s3client.getObject(bucket, fileName);
				S3Object o = s3client.getObject(bucket, fileName);
			    S3ObjectInputStream s3is = o.getObjectContent();
			    byte[] bytes = IOUtils.toByteArray(s3is);
		        HttpHeaders httpHeaders = new HttpHeaders();
		        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		        httpHeaders.setContentLength(bytes.length);
		        httpHeaders.setContentDispositionFormData("attachment", fileName);
				return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
			}
		} catch (AmazonServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in Amazon Access"+" - "+e.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> deleteFile(String fileName) {
		try {
			s3client.deleteObject(bucket, fileName);
		    return ResponseEntity.status(HttpStatus.OK).body(fileName);
		 } catch (AmazonServiceException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem in Amazon Access"+" - "+e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
