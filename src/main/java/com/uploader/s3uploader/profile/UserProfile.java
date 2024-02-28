package com.uploader.s3uploader.profile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserProfile {

    private UUID userProfileId;
    private String userName;
    private String userProfileImageLInk;

    public UserProfile(UUID userProfileId, String userName, String userProfileInageLink){
        this.userProfileId = userProfileId;
        this.userName = userName;
        this.userProfileImageLInk = userProfileInageLink;
    }

    public UUID getUserProfileId() {
        return userProfileId;
    }

    public String getUserName() {
        return userName;
    }
    public Optional<String> getUserProfileImageLInk() {
        return Optional.ofNullable(userProfileImageLInk);
    }

    public void setUserProfileId(UUID userProfileId) {
        this.userProfileId = userProfileId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfileImageLInk(String userProfileImageLInk) {
        this.userProfileImageLInk = userProfileImageLInk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(userProfileId, that.userProfileId) && Objects.equals(userName, that.userName) && Objects.equals(userProfileImageLInk, that.userProfileImageLInk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, userName, userProfileImageLInk);
    }
}
