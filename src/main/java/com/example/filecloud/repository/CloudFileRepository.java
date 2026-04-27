package com.example.filecloud.repository;

import com.example.filecloud.model.CloudFile;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudFileRepository extends JpaRepository<CloudFile, Integer> {
    public List<CloudFile> findByAuthToken(String authToken, Sort sort, Limit limit);
    public Optional<CloudFile> findByFilenameAndAuthToken(String filename, String authToken);
}

