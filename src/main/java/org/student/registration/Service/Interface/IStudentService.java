package org.student.registration.Service.Interface;

import org.student.registration.Model.Student;

import java.util.List;

public interface IStudentService {

    public static String NAME = "StudentServiceImplementation";
     String saveStudent(Student student);
     String updateStudent(Student student);

     List<Student> getStudentList();

     String deleteStudent(Student student);

}
