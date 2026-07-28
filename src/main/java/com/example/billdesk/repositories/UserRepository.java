package com.example.billdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billdesk.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
