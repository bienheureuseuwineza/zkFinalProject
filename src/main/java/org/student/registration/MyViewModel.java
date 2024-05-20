package org.student.registration;
import org.student.registration.Model.Student;
import org.student.registration.Model.User;
import org.student.registration.Service.Interface.IStudentService;
import org.student.registration.Service.Interface.IUserService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import java.util.List;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MyViewModel {

    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    @WireVariable(IUserService.NAME)
    private IUserService userService;
    private Student student;

    private ListModelList<Student> studentListModelList;
    @WireVariable(IStudentService.NAME)
    private IStudentService istudentService;



    @Init
    public void init(){
        this.student = new Student();
        List<Student> studentList = istudentService.getStudentList();
        studentListModelList = new ListModelList<>(studentList);
    }

    @NotifyChange({"studentListModelList","student"})
    public void addStudent(){
        String result = istudentService.saveStudent(student);
        Clients.showNotification(result);
        if ("Student registered successfully".equals(result)) {

            List<Student> studentList = istudentService.getStudentList();
            studentListModelList.clear();
            studentListModelList.addAll(studentList);
        }
        student = new Student();
    }

    @Command
    @NotifyChange({"student", "studentListModelList"})
    public void updateStudent()
    {
        String result = istudentService.updateStudent(student);
        Clients.showNotification(result);
        if ("Student updated Successfully".equals(result)) {
            List<Student> studentList = istudentService.getStudentList();
            studentListModelList.clear();
            studentListModelList.addAll(studentList);
        }
    }

    @Command
    public void confirmDelete() {
        if (student == null) {
            Clients.showNotification("No student selected");
            return;
        }

        Messagebox.show("Are you sure you want to delete this student?",
                "Confirm Delete",
                Messagebox.YES | Messagebox.NO,
                Messagebox.QUESTION,
                event -> {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        deleteStudent();
                    }
                });
    }


    @Command
    @NotifyChange({"studentListModelList","student"})
    public void deleteStudent(){
        if (student != null) {
            String result = istudentService.deleteStudent(student);
            Clients.showNotification(result);
            if ("student deleted".equals(result)) {
                List<Student> studentList = istudentService.getStudentList();
                studentListModelList.clear();
                studentListModelList.addAll(studentList);
            }
            student = new Student();
        } else {
            Clients.showNotification("No student Selected");
        }
    }

    @Command
    public void registerUser() {
        if (name == null || email == null || password == null) {
            Clients.showNotification("Please fill all the fields to register.");
            return;
        }
        User user = userService.registerUser(name, email, password);
        if (user != null) {
            Clients.showNotification("Registration successful!");
        } else {
            Clients.showNotification("Registration failed: Please check your details and try again.");
        }
    }




    @Command
    @NotifyChange("*")
    public void login() {
        User user = userService.loginUser(email, password);
        if (user == null) {
            Messagebox.show("Login failed: Invalid credentials.", "Error", Messagebox.OK, Messagebox.ERROR);

        }
        Messagebox.show("Login successful!", "Success", Messagebox.OK, Messagebox.INFORMATION);
        Executions.sendRedirect("/index.zul");
    }

    public Student getStudent() {

        return student;
    }

    public void setStudent(Student student) {

        this.student = student;
    }

    public ListModelList<Student> getStudentListModelList() {
        return studentListModelList;
    }

    public void setStudentListModelList(ListModelList<Student> studentListModelList) {
        this.studentListModelList = studentListModelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
