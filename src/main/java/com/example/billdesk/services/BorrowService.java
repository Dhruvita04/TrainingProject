package com.example.billdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.billdesk.models.Book;
import com.example.billdesk.models.BorrowRecord;
import com.example.billdesk.models.BorrowStatus;
import com.example.billdesk.models.User;
import com.example.billdesk.repositories.BookRepository;
import com.example.billdesk.repositories.BorrowRecordRepository;
import com.example.billdesk.repositories.UserRepository;

@Service
public class BorrowService {

    private static final int MAX_ACTIVE_BORROWS = 3;
    private static final int LOAN_PERIOD_DAYS = 14;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // Librarian-only action: issues a book to the given member immediately.
    @Transactional
    public BorrowRecord issueBook(String memberUsername, int bookId) {
        User member = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new NoSuchElementException("Member not found: " + memberUsername));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies of \"" + book.getTitle() + "\" are currently available");
        }

        long activeBorrows = borrowRecordRepository.countByUserAndStatus(member, BorrowStatus.BORROWED);
        if (activeBorrows >= MAX_ACTIVE_BORROWS) {
            throw new IllegalStateException(
                    member.getUsername() + " already has " + MAX_ACTIVE_BORROWS + " active borrows.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setUser(member);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
        record.setStatus(BorrowStatus.BORROWED);

        return borrowRecordRepository.save(record);
    }

    // Librarian-only action: marks a record returned, regardless of who it
    // belongs to - the librarian is acting on the member's behalf.
    @Transactional
    public BorrowRecord returnBook(int recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoSuchElementException("Borrow record not found: " + recordId));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("This book has already been returned");
        }

        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    // Member-facing: read-only, scoped to their own records.
    public List<BorrowRecord> getMyRecords(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));

        List<BorrowRecord> records = borrowRecordRepository.findByUser(user);
        flagOverdue(records);
        return records;
    }

    public List<BorrowRecord> getAllRecords() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        flagOverdue(records);
        return records;
    }

    private void flagOverdue(List<BorrowRecord> records) {
        LocalDate today = LocalDate.now();
        for (BorrowRecord r : records) {
            if (r.getStatus() == BorrowStatus.BORROWED && r.getDueDate().isBefore(today)) {
                r.setStatus(BorrowStatus.OVERDUE);
                borrowRecordRepository.save(r);
            }
        }
    }
}