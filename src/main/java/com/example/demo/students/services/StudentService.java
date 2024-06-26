package com.example.demo.students.services;

import com.example.demo.students.models.Student;
import com.example.demo.students.repositories.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Collection<Student> getAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "surname");
        return studentRepository.findAll();
    }

    public Student getBy(long id) throws ClassNotFoundException {
        if (studentRepository.findById(id).isEmpty()) {
            throw new ClassNotFoundException("Student not found");
        }
        return studentRepository.findById(id).get();
    }

    public Student update(long id, Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    public Student updateStatus(long id, boolean working) throws ClassNotFoundException {

        if (studentRepository.findById(id).isEmpty()) {
            throw new ClassNotFoundException("Student not found");
        }
        Student student = studentRepository.findById(id).get();
        student.setIsWorking(working);
        return studentRepository.save(student);
    }

    public void delete(long id) {
        studentRepository.deleteById(id);
    }
}
