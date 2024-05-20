package org.student.registration.Dao;

import org.student.registration.Model.User;

public interface IUserDao {
    User findByEmail(String email);
//    User findById(Integer id);
    String save(User user);
}
