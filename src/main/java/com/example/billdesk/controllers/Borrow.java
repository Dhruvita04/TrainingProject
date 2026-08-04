package com.example.billdesk.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.billdesk.dto.IssueBookRequest;
import com.example.billdesk.models.BorrowRecord;
import com.example.billdesk.services.BorrowService;

@RestController
@RequestMapping("/api/borrow")
public class Borrow {

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BorrowRecord> issueBook(@RequestBody IssueBookRequest request) {
        System.out.println(">>> issueBook() METHOD REACHED. Authenticated as: " +
            SecurityContextHolder.getContext().getAuthentication().getName() +
            " | Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.ok(borrowService.issueBook(request.getUsername(), request.getBookId()));
    }

    @PutMapping("/{recordId}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable int recordId) {
        return ResponseEntity.ok(borrowService.returnBook(recordId));
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