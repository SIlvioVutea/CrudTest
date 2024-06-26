package com.example.demo;

import com.example.demo.students.models.Student;
import com.example.demo.students.repositories.StudentRepository;
import com.example.demo.students.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_test() {
        Student student = new Student();
        student.setName("Silvio");
        student.setSurname("Vutea");
        student.setIsWorking(true);

        Student studentInserted = new Student();
        studentInserted.setId(1);
        studentInserted.setName("Silvio");
        studentInserted.setSurname("Vutea");
        studentInserted.setIsWorking(true);

        when(studentRepository.save(student))
                .thenReturn(studentInserted);
        Student result = studentService.create(student);

        assertEquals(result, studentInserted);
    }



    @Test
    public void getAll_test() {
        Student student = new Student();
        student.setName("Silvio");
        student.setSurname("Vutea");
        student.setIsWorking(true);
        student.setId(1);

        Student student2 = new Student();
        student2.setName("Rocco");
        student2.setSurname("Siff");
        student2.setIsWorking(true);
        student2.setId(2);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student2);
        when((studentRepository.findAll()))
                .thenReturn(students);
        Collection<Student> result = studentService.getAll();

        assertEquals(result, students);
    }

    @Test
    public void getBy_idNotFoundedTest() {
        when(studentRepository.findById(0L))
                .thenReturn(Optional.empty());
        assertThrows(ClassNotFoundException.class, () -> studentService.getBy(0));
    }

    @Test
    public void getBy_test() throws ClassNotFoundException {
        Student student = new Student();
        student.setName("Silvio");
        student.setSurname("Vutea");
        student.setIsWorking(true);
        student.setId(1);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        Student result = studentService.getBy(1L);
        assertEquals(result, student);
    }

    @Test
    public void updateStatus_test() throws ClassNotFoundException {
        Student student = new Student();
        student.setName("Silvio");
        student.setSurname("Vutea");
        student.setIsWorking(true);


        Student studentInserted = new Student();
        studentInserted.setId(1);
        studentInserted.setName("Silvio");
        studentInserted.setSurname("Vutea");
        studentInserted.setIsWorking(false);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(studentRepository.save(student))
                .thenReturn(studentInserted);
        Student result = studentService.updateStatus(1L, false);

        assertEquals(result, studentInserted);
    }
}
