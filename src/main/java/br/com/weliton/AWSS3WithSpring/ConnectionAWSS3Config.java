package br.com.weliton.AWSS3WithSpring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class ConnectionAWSS3Config {
	@Value("${jsa.s3.aws.accessKey}")
	private String awsId;
	 
	@Value("${jsa.s3.aws.secretKey}")
	private String awsKey;
		
	@Value("${jsa.s3.aws.region}")
	private String region;
	
	
	@Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(awsId, awsKey);
    }

	@Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard().withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
    }
	
}
