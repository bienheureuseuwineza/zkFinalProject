package org.student.registration.Service.Interface;

import org.student.registration.Model.User;

public interface IUserService {
    public static String NAME = "UserServiceImplementation";
    User registerUser(String name, String email, String password);
    User loginUser(String email, String password);
}
