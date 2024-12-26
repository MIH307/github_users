package com.example.githubusers.web.rest;

import com.example.githubusers.data.entities.GitHubUserEntity;
import com.example.githubusers.data.repository.GitHubUserRepository;
import com.example.githubusers.web.models.GitHubUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
public class GitHubUsersController {
    private final GitHubUserRepository gitHubUserRepository;

    public GitHubUsersController(GitHubUserRepository gitHubUserRepository){
        this.gitHubUserRepository = gitHubUserRepository;
    }

    @GetMapping("/users")
    public Page<GitHubUser> getPaginatedUsers(@RequestParam int page, @RequestParam int size) {
        return  new PageImpl<>(gitHubUserRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(entity -> transformToWeb(entity))
                .toList());
    }

    private GitHubUser transformToWeb(GitHubUserEntity entityUser){
        GitHubUser webUser = new GitHubUser(entityUser.getGitId(),
                                            entityUser.getLogin(),
                                            entityUser.getAvatarUrl(),
                                            entityUser.getHtmlUrl());

        return webUser;
    }
}
