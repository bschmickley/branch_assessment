package com.schmix.branch.services.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schmix.branch.exceptions.BadUpstreamResponseException;
import com.schmix.branch.exceptions.ResourceNotFoundException;
import com.schmix.branch.models.github.UserData;
import com.schmix.branch.models.github.UserRepo;
import com.schmix.branch.repository.github.UserDataRepository;
import com.schmix.branch.services.HttpService;
import com.schmix.branch.utils.Urls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubService {
    private final HttpService httpService;
    private final UserDataRepository userDataRepository;
    private final Logger logger;
    private final ObjectMapper mapper;

    @Autowired
    public GithubService(HttpService httpService, UserDataRepository userDataRepository) {
        this.httpService = httpService;
        this.userDataRepository = userDataRepository;

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
        UserData data = userDataRepository.get(username);
        if (null != data) {
            logger.info(String.format("Retrieved data for %s from cache", username));
            return data;
        }

        data = fetchUserDataByUsername(username);
        data.setRepos(fetchUserReposByUsername(username));

        userDataRepository.save(username, data);

        return data;
    }

    private UserData fetchUserDataByUsername(String username) {
        String body = httpService.get(Urls.getUserUrl(username));
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

    private UserRepo[] fetchUserReposByUsername(String username) {
        String repoBody = httpService.get(Urls.getUserReposUrl(username));
        if (null == repoBody) {
            logger.info(String.format("Repo request for %s returned NULL", username));
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
