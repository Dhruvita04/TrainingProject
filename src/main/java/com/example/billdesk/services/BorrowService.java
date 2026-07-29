package com.example.billdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.billdesk.models.Book;
import com.example.billdesk.models.BorrowRecord;
import com.example.billdesk.models.BorrowStatus;
import com.example.billdesk.models.User;
import com.example.billdesk.repositories.BookRepository;
import com.example.billdesk.repositories.BorrowRecordRepository;
import com.example.billdesk.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowService {
	
	private static final int MAX_ACTIVE_BORROWS=3;
	private static final int LOAN_PERIOD_DAYS=14;
	
	@Autowired
	private BorrowRecordRepository borrowRecordRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public BorrowRecord borrowBook(String username,int bookId) {
		
		User user=userRepository.findByUsername(username).orElseThrow(()->new NoSuchElementException("User not found"));
		
		Book book=bookRepository.findById(bookId).orElseThrow(()-> new NoSuchElementException("Book not Found"));
		
		if(book.getAvailableCopies()<=0) {
			throw new IllegalStateException("No copies of book \""+book.getTitle()+"\" found");
		}
		
		long activeBorrows=borrowRecordRepository.countByUserAndStatus(user, BorrowStatus.BORROWED);
		if(activeBorrows>=MAX_ACTIVE_BORROWS) {
			 throw new IllegalStateException("You already have " + MAX_ACTIVE_BORROWS + " active borrows. Return a book first.");
		}
		
		book.setAvailableCopies(book.getAvailableCopies()-1);
		bookRepository.save(book);
		
		BorrowRecord record=new BorrowRecord();
		record.setUser(user);
		record.setBook(book);
		record.setBorrowDate(LocalDate.now());
		record.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
		record.setStatus(BorrowStatus.BORROWED);
		
		return borrowRecordRepository.save(record);
		
	}
	@Transactional
	public BorrowRecord returnBook(String username, int recordId) {
		
		BorrowRecord record2=borrowRecordRepository.findById(recordId).orElseThrow(()->new NoSuchElementException("Borrow record not found"));
		
		if(!record2.getUser().getUsername().equals(username)) {
			 throw new IllegalStateException("You can only return your own books");
		}
		
		if(record2.getStatus()==BorrowStatus.RETURNED) {
			
			throw new IllegalStateException("The book has already been returned");
			
		}
		
		record2.setReturnDate(LocalDate.now());
		record2.setStatus(BorrowStatus.RETURNED);
		
		 Book book=record2.getBook();
		 book.setAvailableCopies(book.getAvailableCopies()+1);
		 bookRepository.save(book);
		 
		 return borrowRecordRepository.save(record2);
		 
	}
	
	public List<BorrowRecord> getMyRecords(String username){
		
		User user=userRepository.findByUsername(username).orElseThrow(()->new NoSuchElementException("Username not found"));
		
		List<BorrowRecord> records=borrowRecordRepository.findByUser(user);
		flagOverdue(records);
		return records;
	}
	
	private void flagOverdue(List<BorrowRecord> records) {
		
		LocalDate todayDate=LocalDate.now();
		
		for(BorrowRecord r:records) {
			
			if(r.getStatus()==BorrowStatus.BORROWED && r.getDueDate().isBefore(todayDate)) {
				r.setStatus(BorrowStatus.OVERDUE);
				borrowRecordRepository.save(r);
			}
		}
	}
	public List<BorrowRecord> getAllRecords() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        flagOverdue(records);
        return records;
    }
		
}
