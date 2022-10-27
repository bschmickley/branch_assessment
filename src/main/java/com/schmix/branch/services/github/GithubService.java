package com.schmix.branch.services.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.schmix.branch.exceptions.BadUpstreamResponseException;
import com.schmix.branch.exceptions.ResourceNotFoundException;
import com.schmix.branch.models.github.UserData;
import com.schmix.branch.models.github.UserRepo;
import com.schmix.branch.services.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GithubService {
    private static final String USER_URL = "https://api.github.com/users/%s";
    private static final String USER_REPOS_URL = "https://api.github.com/users/%s/repos";

    private HttpService httpService;
    private final Logger logger;
    private final ObjectMapper mapper;
    private final Cache<String, UserData> cache;

    @Autowired
    public GithubService(HttpService httpService) {
        this.httpService = httpService;

        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build();
        logger = LoggerFactory.getLogger(GithubService.class);
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public UserData getUserData(String username) {
        if (null == username || username.isEmpty()) {
            throw new IllegalArgumentException("Valid username must be provided");
        }

        return getUserDataByUsername(username);
    }

    private UserData getUserDataByUsername(String username) {
        UserData data = cache.getIfPresent(username);
        if (null != data) {
            logger.info(String.format("Retrieved data for %s from cache", username));
            return data;
        }

        data = fetchUserDataByUsername(username);
        data.setRepos(fetchUserRepos(username));

        cache.put(username, data);

        return data;
    }

    private UserData fetchUserDataByUsername(String username) {
        String body = httpService.get(String.format(USER_URL, username));
        if (null == body) {
            throw new ResourceNotFoundException(String.format("Unable to find data for user %s", username));
        }

        try {
            return mapper.readValue(body, UserData.class);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON response", e);
            throw new BadUpstreamResponseException("Unable to parse Github response");
        }
    }

    private UserRepo[] fetchUserRepos(String username) {
        String repoBody = httpService.get(String.format(USER_REPOS_URL, username));
        if (null == repoBody) {
            logger.info(String.format("Repo request for %s returned NULL"));
        } else {
            try {
                return mapper.readValue(repoBody, UserRepo[].class);
            } catch (JsonProcessingException e) {
                // Just log, don't throw. Repos can be null
                logger.error("Error processing UserRepo data", e);
            }
        }

        return null;
    }
}
