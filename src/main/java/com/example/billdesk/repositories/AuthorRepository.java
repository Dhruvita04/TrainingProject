package com.example.billdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billdesk.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

}
