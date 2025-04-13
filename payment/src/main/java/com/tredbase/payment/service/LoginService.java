package com.tredbase.payment.service;

import com.tredbase.payment.dto.request.LoginDto;
import com.tredbase.payment.dto.response.AuthenticationResponse;
import com.tredbase.payment.entities.Admin;
import com.tredbase.payment.entities.BaseUser;
import com.tredbase.payment.entities.Parent;
import com.tredbase.payment.entities.Student;
import com.tredbase.payment.entities.enums.Role;
import com.tredbase.payment.exception.UnAuthorizedException;
import com.tredbase.payment.exception.UserNotFoundException;
import com.tredbase.payment.repository.AdminRepository;
import com.tredbase.payment.repository.ParentRepository;
import com.tredbase.payment.repository.StudentRepository;
import com.tredbase.payment.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtService jwtService;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Transactional
    public  AuthenticationResponse loginUser(LoginDto loginRequest) {

        try {
            // First authenticate using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Check if user is a Parent
            Optional<Parent> parentOpt = parentRepository.findByEmail(loginRequest.getEmail());
            if (parentOpt.isPresent()) {
                Parent parent = parentOpt.get();
                return processSuccessfulLogin(
                        parent,
                        Role.ROLE_PARENT.getRole(),
                        loginRequest.getPassword(),
                        parent.getPassword());
            }

            // Check if user is a Student
            Optional<Student> studentOpt = studentRepository.findByEmail(loginRequest.getEmail());
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                return processSuccessfulLogin(
                        student,
                        Role.ROLE_STUDENT.getRole(),
                        loginRequest.getPassword(),
                        student.getPassword()
                );
            }

            Optional<Admin> adminOpt = adminRepository.findByEmail(loginRequest.getEmail());
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                return processSuccessfulLogin(
                        admin,
                        Role.ROLE_ADMIN.getRole(),
                        loginRequest.getPassword(),
                        admin.getPassword()
                );
            }

            throw new UserNotFoundException();

        } catch (Exception e) {
            throw new UnAuthorizedException("Invalid email or password");
        }
    }

    private AuthenticationResponse processSuccessfulLogin(BaseUser user, String role, String rawPassword, String hashedPassword) {
        if (passwordEncoder.matches(rawPassword, hashedPassword)) {
            String token = jwtService.generateJwtToken(user);
            String name = user.getFirstname() + " " + user.getLastname();
            return new AuthenticationResponse(token, name, role, user.getId(), user.getEmail(), user.getPhoneNumber());
        } else {
            throw new UnAuthorizedException("Incorrect password");
        }
    }

}
