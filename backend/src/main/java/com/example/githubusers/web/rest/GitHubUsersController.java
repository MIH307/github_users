package com.example.githubusers.web.rest;

import com.example.githubusers.data.entities.GitHubUserEntity;
import com.example.githubusers.data.repository.GitHubUserRepository;
import com.example.githubusers.web.models.GitHubUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/github")
public class GitHubUsersController {
    private final GitHubUserRepository gitHubUserRepository;

    public GitHubUsersController(GitHubUserRepository gitHubUserRepository){
        this.gitHubUserRepository = gitHubUserRepository;
    }

    @GetMapping("/users")
    public Page<GitHubUserEntity> getPaginatedUsers(@Valid @RequestParam int page, @RequestParam int size) {
        return gitHubUserRepository.findAll(PageRequest.of(page, size));
    }

    private GitHubUser transformToWeb(GitHubUserEntity entityUser){
        GitHubUser webUser = new GitHubUser(entityUser.getGitId(),
                                            entityUser.getLogin(),
                                            entityUser.getAvatarUrl(),
                                            entityUser.getHtmlUrl());

        return webUser;
    }
}
