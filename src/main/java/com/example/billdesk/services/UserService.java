//package com.example.billdesk.services;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.billdesk.dto.UserRequest;
//import com.example.billdesk.models.User;
//import com.example.billdesk.repositories.UserRepository;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
//
//    public User getById(int id) {
//        return userRepository.findById(id).orElseThrow();
//    }
//
//    public User create(UserRequest request) {
//        User u = new User();
//        u.setName(request.getName());
//        u.setEmail(request.getEmail());
//        u.setPassword(request.getPassword());
//        return userRepository.save(u);
//    }
//
//    public User update(int id, UserRequest request) {
//        User u = userRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
//        u.setName(request.getName());
//        u.setEmail(request.getEmail());
//        u.setPassword(request.getPassword());
//        return userRepository.save(u);
//    }
//
//    public void delete(int id) {
//        User u = userRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
//        userRepository.delete(u);
//    }
//}
