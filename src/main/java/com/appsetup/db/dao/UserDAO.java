package com.appsetup.db.dao;

import com.appsetup.db.entity.User;
import com.appsetup.type.UserRole;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional; 
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import com.google.common.base.Strings;

/**
 *
 * @author Виталий
 */

@Repository
@Transactional
public class UserDAO {
    
    @Autowired
    private SessionFactory sessionFactory;   
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createUser(String name, String email, String password, UserRole role) throws Exception {
        User user = new User();
        try {
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            
            sessionFactory.getCurrentSession().save(user);
        } finally {
        }
        return user;
    }

    public User getUserByLogin(String name) {
        Query q = sessionFactory.getCurrentSession().createQuery("from User where name=:name");
        q.setString("name", name);
        User user = (User) q.uniqueResult();
        return user;
    }
    
    public List<User> findUserByName(String name) {
        
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);//sessionFactory.getCurrentSession().createCriteria(User.class);
        criteria.add(Restrictions.like("name", name+"%"));
        List results = criteria.list();
        return results;
    }
    
    public User updateUser(int userID, String name, String email, String password, String role) throws Exception {

        User user = getUserByID(userID);
        if (!Strings.nullToEmpty(name).isEmpty()) {
            user.setName(name);
        }
        if (!Strings.nullToEmpty(email).isEmpty()) {
            user.setEmail(email);
        }
        if (!Strings.nullToEmpty(password).isEmpty()) {
            user.setPassword(password);
        }
        if (!Strings.nullToEmpty(role).isEmpty()) {
            user.setRole(UserRole.valueOf(role));
        }

        sessionFactory.getCurrentSession().update(user);
        return user;
    }

    public User getUserByID(int id) {

        User user = (User) sessionFactory.getCurrentSession().get(User.class, id);
        return user;
    }
    
    public int deleteByID(int id) {
        User user = getUserByID(id);
        sessionFactory.getCurrentSession().delete(user);
        return 1;
    }
    
}
