package org.student.registration.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.student.registration.Model.Student;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Repository
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StudentImpDao implements IStudentDao{
     SessionFactory sessionFactory;

     @Autowired
    public StudentImpDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PHONE_REGEX = "\\+250[0-9]{9}";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);


    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public String addStudent(Student student) {
        if (!isValidEmail(student.getEmail())) {
            return "Invalid email format";
        }


        if (!isValidPhoneNumber(student.getPhoneNumber())) {
            return "Invalid phone number format";
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Check if the email already exists
            long count = (long) session.createQuery("SELECT COUNT(s.id) FROM Student s WHERE s.email = :email")
                    .setParameter("email", student.getEmail())
                    .uniqueResult();
            if (count > 0) {
                return "Email already exists";
            }

            session.save(student);
            transaction.commit();
            return "Student registered successfully";
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return "Connection failed";
        }

    }



    @Override
    public String updateStudent(Student student) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.update(student);
            transaction.commit();
            return "Student updated Successfully";
        } catch (Exception e) {
            if(transaction != null)
            {
                transaction.rollback();
            } e.printStackTrace();
            return "Connection failed";
        }
    }

    @Override
    public List<Student> getStudentList() {
        try (Session session = sessionFactory.openSession()) {
            // Using try-with-resources to ensure session is closed after use
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            // Log the exception and return an empty list or handle appropriately
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list instead of null
        }
    }

    @Override
    public String deleteStudent(Student student) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            if(student != null) {
                session.delete(student);
                transaction.commit();
                return "student deleted";
            } else {
                return "student not found";
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return "Connection failed";
        }
    }
}
