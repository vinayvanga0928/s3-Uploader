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

        if (file.isEmpty()){
            throw new IllegalStateException("The uploaded file is empty [ "+ file.getSize()+" ]");
        }

        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image ["+ file.getContentType()+" ]");
        }


        UserProfile user = getUserProfileOrThrow(userProfileId);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));


        String path = String.format("%s", BucketName.BUCKET_IMAGE.getBucketName());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Error Uploading file to s3: ", e.fillInStackTrace());
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId){
        String path = String.format("%s", BucketName.BUCKET_IMAGE.getBucketName());
        UserProfile user = getUserProfileOrThrow(userProfileId);
        return user.getUserProfileImageLInk()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }

    public UserProfile getUserProfileOrThrow(UUID userProfileId){
        return userProfileDataAccessService.getUserProfiles()
                .stream().filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("userProfile %s not found", userProfileId)));

    }
}
