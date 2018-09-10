package com.appsetup;

import com.appsetup.db.dao.AppDAO;
import com.appsetup.db.dao.UserDAO;
import com.appsetup.interceptor.AppInterceptor;
import com.appsetup.interceptor.SessionInterceptor;
import com.appsetup.interceptor.UserInterceptor;
import com.appsetup.srv.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
   
    @Autowired
    Roles access;

    @Autowired
    private AppDAO appDAO;

    @Autowired
    private UserDAO userDAO;    
    

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        UserInterceptor userInterceptor = new UserInterceptor();
        userInterceptor.setAccess(access);
        userInterceptor.setUserDAO(userDAO);

        SessionInterceptor sessionInterceptor = new SessionInterceptor();
        sessionInterceptor.setAccess(access);
        sessionInterceptor.setUserDAO(userDAO);
        
        AppInterceptor appInterceptor = new AppInterceptor();
        appInterceptor.setAppDAO(appDAO);
        
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/users/**");
        registry.addInterceptor(appInterceptor).addPathPatterns("/app/**");
        
    }


}
