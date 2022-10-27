package com.schmix.branch.models.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.schmix.branch.models.github.response.UserDataResponse;

public class UserData {
    @JsonProperty("login")
    private String login;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String location;
    private String email;
    @JsonProperty("created_at")
    private String createdAt;
    private UserRepo[] repos;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserRepo[] getRepos() {
        return repos;
    }

    public void setRepos(UserRepo[] repos) {
        this.repos = repos;
    }

    public String toString() {
        return "UserData(" +
                ")";
    }

    public UserDataResponse toResponse() {
        return new UserDataResponse(this);
    }
}
