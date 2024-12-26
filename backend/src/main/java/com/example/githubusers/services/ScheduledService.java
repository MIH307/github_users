package com.example.githubusers.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {
    private final GitHubUserService gitHubUserService;

   public ScheduledService(GitHubUserService gitHubUserService){
        this.gitHubUserService = gitHubUserService;
    }

    //@Scheduled(fixedRate = 300000)
    public void scheduledSync(){
        gitHubUserService.processGitHubUsersSync();
    }
}
