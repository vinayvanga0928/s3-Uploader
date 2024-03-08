package com.uploader.s3uploader.datastore;

import com.uploader.s3uploader.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeUserProfileDataStore {

    private static List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("540e0eba-cfa5-4f6c-90ac-9b5a5f8ac93b"), "VibhaVanga", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("10608bc1-4d38-486f-99af-dd2d9c0fd112"), "VidhuVanga", null));
    }

    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
