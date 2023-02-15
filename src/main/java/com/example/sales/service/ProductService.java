package com.example.sales.service;

import com.example.sales.entity.Category.Product;
import com.example.sales.entity.Category.ProductImage;
import com.example.sales.exception.ResourceNotFoundException;
import com.example.sales.repository.ProductImageRepository;
import com.example.sales.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class ProductService {

    private final AmazonS3Client amazonS3Client;

    private final ProductImageRepository productImageRepository;

    public ProductService(AmazonS3Client amazonS3Client, ProductImageRepository productImageRepository) {
        this.amazonS3Client = amazonS3Client;
        this.productImageRepository = productImageRepository;
    }

    public void uploadProductImage(ProductImage productImage, MultipartFile file) throws IOException {
        String fileUrl = "products/" + productImage.getProduct().getId() + "/" + file.getOriginalFilename();

        // create temporary file from multipart file
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        // upload the file to S3
        amazonS3Client.uploadFile(fileUrl, convFile);

        // set the image URL and save the product image to the database
        productImage.setImageUrl(amazonS3Client.getS3Client().getUrl(amazonS3Client.getBucketName(), fileUrl).toString());
        productImageRepository.save(productImage);

        // delete the temporary file
        convFile.delete();
    }
}
