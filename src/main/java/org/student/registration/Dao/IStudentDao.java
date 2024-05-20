package org.student.registration.Dao;

import org.student.registration.Model.Student;

import java.util.List;

public interface IStudentDao {
    public String addStudent(Student student);

    public String updateStudent(Student student);
    List<Student> getStudentList();

    String deleteStudent(Student student);

}
