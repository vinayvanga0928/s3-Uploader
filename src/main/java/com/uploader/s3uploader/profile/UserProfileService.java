package com.uploader.s3uploader.profile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import com.uploader.s3uploader.bucket.BucketName;
import com.uploader.s3uploader.datastore.FakeUserProfileDataStore;
import com.uploader.s3uploader.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.AbstractDocument;
import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Slf4j
@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore){
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file){

        //check if the image is not empty
        if (file.isEmpty()){
            throw new IllegalStateException("The uploaded file is empty [ "+ file.getSize()+" ]");
        }

        //Check if the file is an image file
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image ["+ file.getContentType()+" ]");
        }

        //Check if the user exists in DB
        UserProfile user = userProfileDataAccessService.getUserProfiles()
                .stream().filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("userProfile %s not found", userProfileId)));

//        get metadata from file
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());

        //Upload the image file to s3
        String path = String.format("%s", BucketName.BUCKET_IMAGE.getBucketName());
        String fileName = String.format("%s-%s", file.getName(), UUID.randomUUID());
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Error Uploading file to s3: ", e.fillInStackTrace());
        }
        //Update DB(userProfileImageLink) with the s3 key
    }
}
