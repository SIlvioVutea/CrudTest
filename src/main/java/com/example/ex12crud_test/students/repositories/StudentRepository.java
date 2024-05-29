package com.example.ex12crud_test.students.repositories;

import com.example.ex12crud_test.students.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.*;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
