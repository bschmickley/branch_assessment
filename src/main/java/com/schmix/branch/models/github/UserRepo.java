package com.schmix.branch.models.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.schmix.branch.models.github.response.UserRepoResponse;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRepo {
    private String name;
    private String htmlUrl;

    public UserRepoResponse toResponse() {
        return new UserRepoResponse(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String toString() {
        return "UserRepo(" +
                String.format("name=%s, ", name) +
                String.format("htmlUrl=%s", htmlUrl) +
                ")";
    }
}
