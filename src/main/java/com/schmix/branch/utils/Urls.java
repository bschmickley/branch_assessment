package com.schmix.branch.utils;

public class Urls {
    private static final String USER_URL = "https://api.github.com/users/%s";
    private static final String USER_REPOS_URL = "https://api.github.com/users/%s/repos";

    public static final String getUserUrl(String username) {
        return String.format(USER_URL, username);
    }

    public static final String getUserReposUrl(String username) {
        return String.format(USER_REPOS_URL, username);
    }

    private Urls() {}
}
