package com.example.bookingDemo.service.admin;

import com.example.bookingDemo.exceptions.UserNotFoundException;
import com.example.bookingDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMaintenanceService {
    @Autowired
    UserRepository userRepository;

    public String deleteUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException("User with id: " + userId + ", not found");
        }

        userRepository.deleteById(userId);

        return "User Deleted";
    }
}
