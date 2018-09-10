package com.appsetup;

import com.appsetup.db.dao.AppDAO;
import com.appsetup.db.dao.UserDAO;
import com.appsetup.srv.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean(name = "access")
    public Roles access() {
        return new Roles();
    }

    @Autowired
    @Bean(name = "userDao")
    public UserDAO userDao() {
        return new UserDAO();
    }

    @Autowired
    @Bean(name = "appDao")
    public AppDAO appDao() {
        return new AppDAO();
    }   
    
}
