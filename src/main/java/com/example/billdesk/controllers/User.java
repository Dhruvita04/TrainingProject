//package com.example.billdesk.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.example.billdesk.controllers.User;
//import com.example.billdesk.dto.UserRequest;
//import com.example.billdesk.services.UserService;
//
//@RestController
//@RequestMapping("/api/users")
//public class User {
//
//	@Autowired
//	private UserService userService;
//
//	@GetMapping
//	public ResponseEntity<List<User>> getAll() {
//		return ResponseEntity.ok(userService.getAll());
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<User> getById(@PathVariable int id) {
//		return ResponseEntity.ok(userService.getById(id));
//	}
//
//	@PostMapping
//	public ResponseEntity<User> create(@RequestBody UserRequest request) {
//		return ResponseEntity.ok(userService.create(request));
//	}
//
//	@PutMapping("/{id}")
//	public ResponseEntity<User> update(@PathVariable int id, @RequestBody UserRequest request) {
//		return ResponseEntity.ok(userService.update(id, request));
//	}
//
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> delete(@PathVariable int id) {
//		userService.delete(id);
//		return ResponseEntity.noContent().build();
//	}
//}
