package com.schmix.branch.services.github;

import com.schmix.branch.GithubData;
import com.schmix.branch.exceptions.ResourceNotFoundException;
import com.schmix.branch.models.github.UserData;
import com.schmix.branch.repository.github.UserDataRepository;
import com.schmix.branch.services.HttpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class GithubServiceTest {
    private final HttpService httpService = Mockito.spy(HttpService.class);
    private final UserDataRepository userDataRepository = Mockito.spy(UserDataRepository.class);
    private final GithubService githubService = new GithubService(httpService, userDataRepository);

    @Test
    public void getUserData_Success() {
        String username = "username";

        doReturn(GithubData.USER_DATA, GithubData.USER_REPOS).when(httpService).get(nullable(String.class));

        UserData data = githubService.getUserData(username);
        Assertions.assertNotNull(data.getRepos());
        Assertions.assertTrue(data.getRepos().length > 0);
    }

    @Test
    public void getUserData_NullOrEmptyUsername() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> githubService.getUserData(null));
    }

    @Test
    public void getUserData_fetchUserDataByUserName_HttpServiceReturnsNull() {
        String username = "username";

        doReturn(null).when(httpService).get(nullable(String.class));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> githubService.getUserData(username));
    }

    @Test
    public void getUserData_fetchUserReposByUserName_HttpServiceReturnsNull() {
        String username = "username";

        doReturn(GithubData.USER_DATA, (String) null).when(httpService).get(nullable(String.class));

        UserData data = githubService.getUserData(username);
        Assertions.assertNull(data.getRepos());
    }
}
