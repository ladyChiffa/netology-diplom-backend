package com.example.filecloud.repository;

import com.example.filecloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByNameAndPassphrase(String name, String passphrase);
    public Optional<User> findByAuthToken(String authToken);
}
