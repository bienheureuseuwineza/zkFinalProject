package org.student.registration.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.student.registration.Dao.StudentImpDao;
import org.student.registration.Model.Student;
import org.student.registration.Service.Interface.IStudentService;

import java.util.List;

@Service(IStudentService.NAME)
public class StudentServiceImplementation implements IStudentService {

 private final     StudentImpDao studentImpDao;

    @Autowired
    public StudentServiceImplementation(StudentImpDao studentImpDao) {
        this.studentImpDao = studentImpDao;

    }



    @Transactional
    @Override
    public String saveStudent(Student student) {
        try {
            String result = studentImpDao.addStudent(student);
            if ("Email already in use".equals(result)) {
                return "Registration failed: Email already in use";
            } else if ("Invalid email format".equals(result)) {
                return "Registration failed: Invalid email format";
            }  else if ("Invalid phone number format".equals(result)) {
                return "Registration failed: Invalid phone number format! please try by starting with +250";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to register student";
        }

    }

    @Transactional
    @Override
    public String updateStudent(Student student) {
        try {
            return studentImpDao.updateStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update student";
        }
    }

    @Override
    public List<Student> getStudentList() {

            return studentImpDao.getStudentList();
    }

    @Override
    public String deleteStudent(Student student) {
        try{
            return studentImpDao.deleteStudent(student);

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to delete student";
        }
    }
}
