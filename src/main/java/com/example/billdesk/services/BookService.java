package com.example.billdesk.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.billdesk.dto.BookRequest;
import com.example.billdesk.models.Author;
import com.example.billdesk.models.Book;
import com.example.billdesk.repositories.AuthorRepository;
import com.example.billdesk.repositories.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(int id) {
        return bookRepository.findById(id).orElseThrow();
        // Still a plain orElseThrow() here on purpose - Phase 8 replaces this
        // with a proper exception + clean JSON error response.
    }

    public Book create(BookRequest request) {
        Author author = null;
        if (request.getAuthorId() != null) {
            author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new NoSuchElementException("Author not found: " + request.getAuthorId()));
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setGenre(request.getGenre());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies()); // starts fully available
        book.setAuthor(author);

        return bookRepository.save(book);
    }
}