package com.uploader.s3uploader.profile;

import com.uploader.s3uploader.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;

    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService){
        this.userProfileDataAccessService = userProfileDataAccessService;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file){

        //check if the image is not empty
        //Check if the file is an image file
        String fileName = file.getName();

        if (file.isEmpty() && !isImageFile(fileName)){
            System.out.println("Uploaded file is either empty or not an image file");
        }
        //Check if the user exists in DB
        //Check if the link field is empty

        //Upload the image file to s3
        //Update DB(userProfileImageLink) with the s3 key
    }

    public boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp");
    }
}
