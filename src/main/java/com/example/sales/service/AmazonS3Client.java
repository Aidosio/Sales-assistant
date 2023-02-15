package com.example.sales.service;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AmazonS3Client {

    private AmazonS3 s3client;

    private String bucketName;

    public AmazonS3Client(AmazonS3 s3client, String bucketName) {
        this.s3client = s3client;
        this.bucketName = bucketName;
    }

    public AmazonS3 getS3Client() {
        return s3client;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void uploadFile(String keyName, File file) {
        s3client.putObject(bucketName, keyName, file);
    }
}