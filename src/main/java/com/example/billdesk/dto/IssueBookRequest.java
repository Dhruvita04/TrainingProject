package com.example.billdesk.dto;

public class IssueBookRequest {

    private String username; // the member to issue the book to
    private int bookId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
}