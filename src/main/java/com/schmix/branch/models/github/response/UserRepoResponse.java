package com.schmix.branch.models.github.response;

import com.schmix.branch.models.github.UserRepo;

public class UserRepoResponse {
    private String name;
    private String url;

    public UserRepoResponse(UserRepo repo) {
        name = repo.getName();
        url = repo.getHtmlUrl();
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
