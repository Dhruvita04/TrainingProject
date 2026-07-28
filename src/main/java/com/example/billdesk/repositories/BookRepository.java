package com.example.billdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billdesk.models.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
