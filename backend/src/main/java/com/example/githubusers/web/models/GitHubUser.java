package com.example.githubusers.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUser(
        Integer id,
        String login,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("html_url") String htmlUrl
) {}
