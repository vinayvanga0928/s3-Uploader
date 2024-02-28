package com.uploader.s3uploader.bucket;

public enum BucketName {

    BUCKET_IMAGE("uploader-image-s3");

    public final String bucketName;

    BucketName(String bucketName){
        this.bucketName = bucketName;
    }

    public String getBucketName(){
        return this.bucketName;
    }
}
