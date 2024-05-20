package org.student.registration.Dao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.student.registration.Model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Slf4j
@Repository
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDao implements IUserDao{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User findByEmail(String email) {
        String queryStr = "FROM User WHERE email = :email";
        TypedQuery<User> query = entityManager.createQuery(queryStr, User.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }


//    @Transactional(readOnly = true)
//    public User findById(Integer id) {
//        return entityManager.find(User.class,id);
//    }


    @Transactional
    public String save(User user) {
        try {

            entityManager.persist(user);
            return "user saved";
        } catch (Exception exception) {
            log.error("Error saving user: " + exception.getLocalizedMessage());
            return "not saved";
        }
    }

}
