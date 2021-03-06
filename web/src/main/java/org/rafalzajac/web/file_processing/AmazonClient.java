package org.rafalzajac.web.file_processing;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazon.s3.region}")
    private String region;
    @Value("${amazon.s3.endpointUrl}")
    private String endpointUrl;
    @Value("${amazon.s3.bucketName}")
    private String bucketName;
    @Value("${amazon.s3.accessKey}")
    private String accessKey;
    @Value("${amazon.s3.secretKey}")
    private String secretKey;



    @PostConstruct
    private void initializeAmazon() {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).withRegion(region).build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException  {

            File convFile = new File(file.getOriginalFilename());

            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }


        return convFile;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, "ScoutFiles/" + fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile, String fileName) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileUrl = "https':'//" + bucketName + "." + endpointUrl + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            Files.delete(Paths.get(file.toURI()));
        } catch (IOException e) {
            log.info("IO File exception", e);
        }

        return fileUrl;
    }

    public S3Object getObjectFromServer(String fileName){
        return s3client.getObject(bucketName, "ScoutFiles/" + fileName);
    }

}
