package com.example.githubusers.services;

import com.example.githubusers.data.entities.GitHubUserEntity;
import com.example.githubusers.data.repository.GitHubUserRepository;
import com.example.githubusers.web.models.GitHubUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    private final String GITHUB_URL = "https://api.github.com/users?since=";

    @Value("${github.token}")
    private String gitHubToken;

    public GitHubUserService(GitHubUserRepository gitHubUserRepository){
        this.gitHubUserRepository = gitHubUserRepository;
    }


    public void processGitHubUsersSync() {
        System.out.println("syncGitHubUsers START!");
        long lastUserId = 0;

        for (int i = 0; i < 10; i++) {
            List<GitHubUserEntity> responseUsers = new ArrayList<>();
            String url = GITHUB_URL + lastUserId + "&per_page=100";

            List<GitHubUser> users = fetchUsersFromGitHub(url);

            if (users == null || users.isEmpty()) {
                break;
            }

            users.forEach(user ->
                    responseUsers.add(transformToDb(user))
            );


            syncUsersWithDatabase(responseUsers);

            lastUserId = users.get(users.size() - 1).id();
        }
        System.out.println("syncGitHubUsers ENDED!  " );
    }

    public void syncUsersWithDatabase(List<GitHubUserEntity> users) {
        createOrUpdateGitHubUsers(listOfUsersForSave(users, getExistingUsers(users)));
    }

    @Transactional
    public void createOrUpdateGitHubUsers(List<GitHubUserEntity> usersToSave){
        gitHubUserRepository.saveAll(usersToSave);
    }

    private List<GitHubUser> fetchUsersFromGitHub(String url){
        ResponseEntity<List<GitHubUser>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                getHttpEntity(),
                new ParameterizedTypeReference<List<GitHubUser>>() {}
        );

        return response.getBody();
    }


    private HttpEntity<String> getHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gitHubToken);

        return new HttpEntity<>(headers);
    }

    private List<GitHubUserEntity> listOfUsersForSave(List<GitHubUserEntity> usersFromGitHub, Map<Integer, GitHubUserEntity> existingUsersMap){
        List<GitHubUserEntity> usersToSave = new ArrayList<>();

        for (GitHubUserEntity user : usersFromGitHub) {
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
        return usersToSave;
    }

    private Map<Integer, GitHubUserEntity> getExistingUsers(List<GitHubUserEntity> users){
        List<Integer> userGitIds = users.stream().map(GitHubUserEntity::getGitId).toList();
        List<GitHubUserEntity> existingUsers = gitHubUserRepository.findAllByGitIdIn(userGitIds);

        Map<Integer, GitHubUserEntity> existingUsersMap = existingUsers.stream()
                .collect(Collectors.toMap(GitHubUserEntity::getGitId, Function.identity()));

        return existingUsersMap;
    }


    private GitHubUserEntity transformToDb(GitHubUser gitHubUser){
        GitHubUserEntity resultEntity = new GitHubUserEntity();
        resultEntity.setGitId(gitHubUser.id());
        resultEntity.setLogin(gitHubUser.login());
        resultEntity.setAvatarUrl(gitHubUser.avatarUrl());
        resultEntity.setHtmlUrl(gitHubUser.htmlUrl());

        return resultEntity;
    }
}
