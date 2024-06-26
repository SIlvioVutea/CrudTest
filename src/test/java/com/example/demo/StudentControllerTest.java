package com.example.demo;

import com.example.demo.students.controllers.StudentController;
import com.example.demo.students.models.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentControllerTest {

    @Autowired
    private StudentController studentController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult createStudentRequest(Student student) throws Exception {
        String studentJson = objectMapper.writeValueAsString(student);
        return this.mockMvc.perform(post("/v1/students").content(studentJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }

    private void insertStudent() throws Exception {
        Student student = createStudent();
        createStudentRequest(student);
    }

    private Student createStudent() {
        Student student = new Student();
        student.setName("Silvio");
        student.setSurname("Vutea");
        student.setIsWorking(false);
        return student;
    }

    private Student getStudent(Student student) throws Exception {
        MvcResult result = createStudentRequest(student);
        return objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
    }

    @Test
    void studentControllerLoads() {
        assertThat(studentController).isNotNull();
    }

    @Test
    void createStudent_test() throws Exception {
        Student student = createStudent();
        MvcResult result = createStudentRequest(student);

        Student actual = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(actual.getName()).isEqualTo("Silvio");
    }


    @Test
    void retrieveStudent_test() throws Exception {
        insertStudent();
        this.mockMvc.perform(get("/v1/students/1"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().json("""
                        {"id": 1,
                        "name": "Silvio",
                        "surname": "Vutea",
                        "isWorking": false
                        }
                        """))
                .andReturn();
    }

    @Test
    void retrieveStudentsList_test() throws Exception {
        insertStudent();
        insertStudent();
        this.mockMvc.perform(get("/v1/students"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andReturn();
    }

    @Test
    void updateStudent_test() throws Exception {
        Student student = createStudent();
        Student registredStudent = getStudent(student);
        registredStudent.setName("Paolo");
        String studentJson = objectMapper.writeValueAsString(registredStudent);
        this.mockMvc.perform(put("/v1/students/" + registredStudent.getId()).content(studentJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().json("""
                        {
                        "name": "Paolo",
                        "surname": "Vutea",
                        "isWorking": false
                        }
                        """))
                .andReturn();
    }

    @Test
    void updateStudentWorkingStatus_test() throws Exception {
        Student student = createStudent();
        Student registredStudent = getStudent(student);
        String studentJson = objectMapper.writeValueAsString(registredStudent);
        this.mockMvc.perform(put("/v1/students/" + registredStudent.getId() + "/status").content(studentJson).contentType(MediaType.APPLICATION_JSON).param("working", "true"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().json("""
                        {
                        "name": "Silvio",
                        "surname": "Vutea",
                        "isWorking": true
                        }
                        """))
                .andReturn();
    }

    @Test
    void deleteStudent_test() throws Exception {
        Student student = createStudent();
        Student registredStudent = getStudent(student);
        Student student2 = createStudent();
        createStudentRequest(student2);

        this.mockMvc.perform(delete("/v1/students/" + registredStudent.getId()))
                .andDo(print());

        this.mockMvc.perform(get("/v1/students")).andDo(print()).andExpect(jsonPath("$", hasSize(1)));
    }

}
