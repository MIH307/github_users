package com.example.githubusers.data.repository;

import com.example.githubusers.data.entities.GitHubUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GitHubUserRepository extends JpaRepository<GitHubUserEntity, Long> {
    public List<GitHubUserEntity> findAllByGitIdIn(List<Integer> gitIds);
}
