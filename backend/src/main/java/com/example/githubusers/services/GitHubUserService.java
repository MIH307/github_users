package com.example.githubusers.services;

import com.example.githubusers.data.entities.GitHubUserEntity;
import com.example.githubusers.data.repository.GitHubUserRepository;
import com.example.githubusers.web.models.GitHubUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GitHubUserService {
    private final GitHubUserRepository gitHubUserRepository;
    private final RestTemplate restTemplate = new RestTemplate();


    public GitHubUserService(GitHubUserRepository gitHubUserRepository){
        this.gitHubUserRepository = gitHubUserRepository;
    }


    public void processGitHubUsersSync() {
        System.out.println("syncGitHubUsers START!");
        long lastUserId = 0;

        for (int i = 0; i < 10; i++) {
            List<GitHubUserEntity> responseUsers = new ArrayList<>();
            String url = "https://api.github.com/users?since=" + lastUserId + "&per_page=100";
            ResponseEntity<List<GitHubUser>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<GitHubUser>>() {}
            );

            List<GitHubUser> users = response.getBody();
            if (users == null || users.isEmpty()) break;

            users.forEach(entity -> responseUsers.add(transformToDb(entity)));
            syncUsersWithDatabase(responseUsers);

            lastUserId = users.get(users.size() - 1).getId();
        }
        System.out.println("syncGitHubUsers ENDED!  " );
    }

    public void syncUsersWithDatabase(List<GitHubUserEntity> users) {
        List<Integer> userGitIds = users.stream().map(GitHubUserEntity::getGitId).toList();
        List<GitHubUserEntity> existingUsers = gitHubUserRepository.findAllByGitIdIn(userGitIds);

        Map<Integer, GitHubUserEntity> existingUsersMap = existingUsers.stream()
                .collect(Collectors.toMap(GitHubUserEntity::getGitId, Function.identity()));

        List<GitHubUserEntity> usersToSave = new ArrayList<>();

        for (GitHubUserEntity user : users) {
            if (existingUsersMap.containsKey(user.getGitId())) {
                GitHubUserEntity existingUser = existingUsersMap.get(user.getGitId());
                existingUser.setLogin(user.getLogin());
                existingUser.setAvatarUrl(user.getAvatarUrl());
                existingUser.setHtmlUrl(user.getHtmlUrl());
                usersToSave.add(existingUser);
            } else {
                usersToSave.add(user);
            }
        }

        createOrUpdateGitHubUsers(usersToSave);
    }

    @Transactional
    public void createOrUpdateGitHubUsers(List<GitHubUserEntity> usersToSave){
        gitHubUserRepository.saveAll(usersToSave);
    }


    private GitHubUserEntity transformToDb(GitHubUser gitHubUser){
        GitHubUserEntity resultEntity = new GitHubUserEntity();
        resultEntity.setGitId(gitHubUser.getId());
        resultEntity.setLogin(gitHubUser.getLogin());
        resultEntity.setAvatarUrl(gitHubUser.getAvatarUrl());
        resultEntity.setHtmlUrl(gitHubUser.getHtmlUrl());

        return resultEntity;
    }
}
