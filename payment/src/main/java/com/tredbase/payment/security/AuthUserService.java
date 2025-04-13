package com.tredbase.payment.security;

import com.tredbase.payment.entities.Admin;
import com.tredbase.payment.entities.Parent;
import com.tredbase.payment.entities.Student;
import com.tredbase.payment.repository.AdminRepository;
import com.tredbase.payment.repository.ParentRepository;
import com.tredbase.payment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService implements UserDetailsService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);
        // Check if the user exists in ParentRepository
        Parent parent = parentRepository.findByEmail(username).orElse(null);
        if (parent != null) {
            log.info("Found parent with username: {}", username);
            return new org.springframework.security.core.userdetails.User(
                    parent.getEmail(),
                    parent.getPassword(),
                    parent.getAuthorities()
            );
        }

        // Check if the user exists in StudentRepository
        Student student = studentRepository.findByEmail(username).orElse(null);
        if (student != null) {
            log.info("Found student with username: {}", username);
            return new org.springframework.security.core.userdetails.User(
                    student.getEmail(),
                    student.getPassword(),
                    student.getAuthorities()
            );
        }

        // Check if the user exists in AdminRepository
        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if (admin != null) {
            log.info("Found admin with username: {}", username);
            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getPassword(),
                    admin.getAuthorities()
            );
        }
        log.error("User with username {} not found", username);
        throw new UsernameNotFoundException("User not found");
    }


}