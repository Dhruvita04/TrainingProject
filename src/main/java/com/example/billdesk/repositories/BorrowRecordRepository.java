package com.example.billdesk.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.billdesk.models.BorrowRecord;
import com.example.billdesk.models.BorrowStatus;
import com.example.billdesk.models.User;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer>{
	
	List<BorrowRecord> findByUser(User user);
	
	long countByUserAndStatus(User user, BorrowStatus status);	

}
