package com.uploader.s3uploader.datastore;

import com.uploader.s3uploader.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeUserProfileDataStore {

    private static List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "VibhaVanga", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "VidhuVanga", null));
    }

    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
