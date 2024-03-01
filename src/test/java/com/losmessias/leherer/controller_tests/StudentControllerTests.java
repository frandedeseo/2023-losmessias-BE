package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.StudentController;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.repository.StudentRepository;
import com.losmessias.leherer.role.AppUserSex;
import com.losmessias.leherer.service.AppUserService;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.JwtService;
import com.losmessias.leherer.service.StudentService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private StudentService studentService;
    @MockBean
    private AppUserService appUserService;

    @MockBean
    private ClassReservationService classReservationService;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    @DisplayName("Get all students gets empty list")
    void testGetAllStudentsReturnsOk() throws Exception {
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/student/all"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all students")
    void testGetAllStudentsReturnsNotFound() throws Exception {
        Student student1 = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        Student student2 = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        ArrayList<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        when(studentService.getAllStudents()).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/student/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Get student by id")
    void testGetStudentByIdReturnsOk() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(new Student());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Get student by id returns not found")
    void testGetStudentByIdReturnsNotFound() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/student/1"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser
    @DisplayName("Add student")
    void testAddStudentReturnsOk() throws Exception {
        JSONObject jsonContent = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(1L);
        jsonContent.put("firstName", "John");
        jsonContent.put("lastName", "Doe");
        jsonContent.put("email", "email");
        jsonContent.put("location", "location");

        when(studentService.create(new Student())).thenReturn(new Student());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/student/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("Add student returns bad request")
    void testAddStudentReturnsBadRequest() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("id", 1L);
        when(studentService.create(new Student())).thenReturn(new Student());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/student/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Add reservation to student")
    void testAddReservationToStudentReturnsOk() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(new Student());
        when(classReservationService.getReservationById(1L)).thenReturn(new ClassReservation());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/student/addReservation")
                        .param("studentId", "1")
                        .param("reservationId", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Add reservation to student returns bad request when student not found")
    void testAddReservationToStudentReturnsBadRequest() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(null);
        when(classReservationService.getReservationById(1L)).thenReturn(new ClassReservation());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/student/addReservation")
                        .param("studentId", "1")
                        .param("reservationId", "1")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Add reservation to student returns bad request when reservation not found")
    void testAddReservationToStudentReturnsBadRequest2() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(new Student());
        when(classReservationService.getReservationById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/student/addReservation")
                        .param("studentId", "1")
                        .param("reservationId", "1")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Update student")
    void testUpdateStudentReturnsOk() throws Exception {
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        studentService.create(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(appUserService.update(any(), any())).thenReturn(student);
        when(studentService.getStudentById(1L)).thenReturn(student);
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("firstName", "John");
        jsonContent.put("lastName", "Doe");
        jsonContent.put("email", "email");
        jsonContent.put("location", "location");
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/student/update/1")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Update student returns bad request when student not found")
    void testUpdateStudentReturnsBadRequest() throws Exception {
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        studentService.create(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(appUserService.update(any(), any())).thenReturn(student);
        when(studentService.getStudentById(1L)).thenReturn(null);
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("firstName", "John");
        jsonContent.put("lastName", "Doe");
        jsonContent.put("email", "email");
        jsonContent.put("location", "location");
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/student/update/1")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Update student returns bad request when student id is null")
    void testUpdateStudentReturnsBadRequest2() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("id", null);
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/student/update/1")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
