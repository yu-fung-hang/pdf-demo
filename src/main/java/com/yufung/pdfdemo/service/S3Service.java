package com.yufung.pdfdemo.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.yufung.pdfdemo.model.PublicFileItem;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {
    AmazonS3 s3Client = null;

    @PostConstruct
    public void initialize() {
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("AKIAU6P3MUKUNBU2WGXG", "vpJ+/Dms2BgMh5wqUx2kn8ZqeteRSInTfWH6bMxx")))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public List<PublicFileItem> listPublicFile(String bucket, String prefix) {
        List<S3ObjectSummary> list = s3Client.listObjects(bucket, prefix).getObjectSummaries();
        return  list.stream()
                .filter(it->it.getSize() > 0)
                .map(it->new PublicFileItem(String.format("https://s3.amazonaws.com/%s/%s", bucket, it.getKey())))
                .sorted(Comparator.comparing(PublicFileItem::getName))
                .collect(Collectors.toList());
    }

    public String putPublicFile(String bucket, String name, InputStream in, long size, String contentType, CannedAccessControlList acl) {
        ObjectMetadata metadata = new ObjectMetadata();
        //metadata.setContentLength(size);
        //metadata.setContentType(contentType);
        s3Client.putObject(new PutObjectRequest(bucket, name, in, metadata).withCannedAcl(acl));
        return String.format("https://s3.amazonaws.com/%s/%s", bucket, name);
    }

    public void removePublicFile(String bucket, String name) {
        s3Client.deleteObject(bucket, name);
    }

    public Boolean isFileExisting(String bucket, String name) {
        return s3Client.doesObjectExist(bucket, name);
    }
}