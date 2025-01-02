package com.example.githubusers.data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "github_users")
@Data
public class GitHubUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "git_id", nullable = false)
    private Integer gitId;

    @Column(nullable = false)
    private String login;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "html_url")
    private String htmlUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public Integer getGitId() {
        return gitId;
    }

    public void setGitId(Integer gitId) {
        this.gitId = gitId;
    }
}
