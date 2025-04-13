package com.tredbase.payment.repository;

import com.tredbase.payment.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository <Student,Long> {
    Optional<Student> findByEmail(String email);
}
