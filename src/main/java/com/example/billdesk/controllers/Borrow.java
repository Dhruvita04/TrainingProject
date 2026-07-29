package com.example.billdesk.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.billdesk.models.BorrowRecord;
import com.example.billdesk.services.BorrowService;

@RestController
@RequestMapping("/api/borrow")
public class Borrow {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/{bookId}")
    public ResponseEntity<BorrowRecord> borrowBook(@PathVariable int bookId, Authentication auth) {
        // auth.getName() returns the username - this is the same value
        // JwtAuthFilter pulled out of the token back in Phase 4, now available
        // here with zero extra code because Spring Security wires it through.
        return ResponseEntity.ok(borrowService.borrowBook(auth.getName(), bookId));
    }

    @PutMapping("/{recordId}/return")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable int recordId, Authentication auth) {
        return ResponseEntity.ok(borrowService.returnBook(auth.getName(), recordId));
    }

    @GetMapping("/my-records")
    public ResponseEntity<List<BorrowRecord>> myRecords(Authentication auth) {
        return ResponseEntity.ok(borrowService.getMyRecords(auth.getName()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BorrowRecord>> allRecords() {
        return ResponseEntity.ok(borrowService.getAllRecords());
    }
}