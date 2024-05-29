package com.example.ex12crud_test.students.controllers;

import com.example.ex12crud_test.students.models.Student;
import com.example.ex12crud_test.students.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/v1/students")
public class StudentController {

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<String> getException(ClassNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(student));
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> loadAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(studentService.getAll());
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{id}")
    public Student loadBy(@PathVariable long id) throws ClassNotFoundException {
        return studentService.getBy(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable long id, @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(studentService.update(id, student));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}/status")
    public Student updateStatus(@PathVariable long id, @RequestParam boolean working) throws ClassNotFoundException {
        return studentService.updateStatus(id, working);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        studentService.delete(id);
    }
}
