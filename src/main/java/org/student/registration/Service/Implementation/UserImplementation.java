package org.student.registration.Service.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.student.registration.Dao.UserDao;
import org.student.registration.Model.User;
import org.student.registration.Service.Interface.IUserService;

@Slf4j
@Service(IUserService.NAME)
public class UserImplementation implements IUserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(String name, String email, String password) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        try {
            userDao.save(user);
            return user;
        } catch (Exception e) {
            log.error("Error registering user: " + e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public User loginUser(String email, String password) {
        User user = userDao.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
