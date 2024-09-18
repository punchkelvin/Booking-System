package com.example.bookingDemo.service;

import com.example.bookingDemo.model.User;
import com.example.bookingDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + ", not found"));

        //Convert User Role to Granted Authority Object
        Collection<GrantedAuthority> authorities =
                Arrays.asList(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), authorities);
    }
}
