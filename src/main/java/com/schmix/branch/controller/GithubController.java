package com.schmix.branch.controller;

import com.schmix.branch.models.github.response.UserDataResponse;
import com.schmix.branch.services.github.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/github")
public class GithubController {
    private GithubService githubService;

    @Autowired
    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserDataResponse getUserData(@PathVariable String username) {
        return githubService.getUserData(username).toResponse();
    }
}
