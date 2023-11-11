package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student addReservationTo(Student student, ClassReservation classReservation) {
        student.addReservation(classReservation);
        return studentRepository.save(student);
    }

    public void setFeedback(Student student, Double rating, Boolean material, Boolean punctuality, Boolean educated ){
        student.receiveFeedback(rating, material, punctuality, educated);
        studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student studentToUpdate = studentRepository.findById(id).orElse(null);
        studentToUpdate.setFirstName(student.getFirstName() != null ? student.getFirstName() : studentToUpdate.getFirstName());
        studentToUpdate.setLastName(student.getLastName() != null ? student.getLastName() : studentToUpdate.getLastName());
        studentToUpdate.setEmail(student.getEmail() != null ? student.getEmail() : studentToUpdate.getEmail());
        studentToUpdate.setLocation(student.getLocation() != null ? student.getLocation() : studentToUpdate.getLocation());
        studentToUpdate.setPhone(student.getPhone() != null ? student.getPhone() : studentToUpdate.getPhone());
        studentToUpdate.setClassReservations(student.getClassReservations() != null ? student.getClassReservations() : studentToUpdate.getClassReservations());
        return studentRepository.save(studentToUpdate);
    }
}
