package com.example.billdesk.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.billdesk.models.Author;
import com.example.billdesk.repositories.AuthorRepository;

@RestController
@RequestMapping("/api/authors")
public class Authors {
	
	@Autowired
	private AuthorRepository ar;
	
	 @GetMapping
	    public ResponseEntity<List<Author>> getAll() {
	        return ResponseEntity.ok(ar.findAll());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Author> getById(@PathVariable int id) {
	        return ResponseEntity.ok(ar.findById(id).orElseThrow());
	    }

	    @PostMapping
	    public ResponseEntity<Author> create(@RequestBody Author author) {
	        Author saved = ar.save(author);
	        return ResponseEntity.ok(saved);
	    }
}
