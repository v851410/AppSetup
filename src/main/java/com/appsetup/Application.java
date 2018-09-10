package com.appsetup;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class Application {
    
    @RequestMapping("/logout")
    @ResponseBody
    String logout(HttpServletRequest request,HttpServletResponse response,Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "{\"logout\":\"ok\"}";
    }

    @RequestMapping("/printErrot")
    @ResponseBody
    ResponseEntity<Object> accessDenied(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Map mess = new HashMap();
        String typeError = "error";
        String messError = "true";
        if (request.getAttribute("typeError") != null && !request.getAttribute("typeError").toString().isEmpty()) {
            typeError = request.getAttribute("typeError").toString();
        }
        if (request.getAttribute("messError") != null && !request.getAttribute("messError").toString().isEmpty()) {
            messError = request.getAttribute("messError").toString();
        }
        mess.put(typeError, messError);
        return new ResponseEntity<>(mess, HttpStatus.OK);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    
    @Autowired
    private Environment env;  
    
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }
 
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
        Properties properties = new Properties();
 
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("current_session_context_class", //
                env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
        properties.put("connection.pool_size",10); 
        
        properties.put("hibernate.dbcp.initialSize",1); 
        properties.put("hibernate.dbcp.maxActive",5); 
        properties.put("hibernate.dbcp.maxIdle",5); 
        properties.put("hibernate.dbcp.minIdle",0); 
        properties.put("spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true",true); 
        properties.put("hibernate.enable_lazy_load_no_trans",true); 
 
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
 
        // Package contain entity classes
        factoryBean.setPackagesToScan(new String[] { "com.appsetup.db.entity" });
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();
        
        //
        SessionFactory sf = factoryBean.getObject();
        return sf;
    }
    
    @Autowired
    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }   
    
    
    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
 
        return transactionManager;
    }    



}
