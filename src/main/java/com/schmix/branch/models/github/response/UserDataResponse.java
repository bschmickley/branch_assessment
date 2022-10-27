package com.schmix.branch.models.github.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.schmix.branch.models.github.UserData;
import com.schmix.branch.models.github.UserRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDataResponse {
    @JsonProperty("user_name")
    private String username;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("avatar")
    private String avatarUrl;
    @JsonProperty("geo_location")
    private String geoLocation;
    private String email;
    @JsonProperty("created_at")
    private String createdAt;
    private UserRepoResponse[] repos;

    public UserDataResponse(UserData data) {
        username = data.getLogin();
        displayName = data.getName();
        avatarUrl = data.getAvatarUrl();
        geoLocation = data.getLocation();
        email = data.getEmail();
        createdAt = reformatDate(data.getCreatedAt());
        repos = null;

        UserRepo[] userRepos = data.getRepos();
        if (null != userRepos) {
            repos = new UserRepoResponse[userRepos.length];

            for (int i = 0; i < userRepos.length; i++) {
                repos[i] = userRepos[i].toResponse();
            }
        }
    }

    private String reformatDate(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(dateStr, inputFormatter);
        return outputFormatter.format(date);
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserRepoResponse[] getRepos() {
        return repos;
    }
}
