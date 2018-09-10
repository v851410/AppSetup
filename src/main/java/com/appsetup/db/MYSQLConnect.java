package com.appsetup.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author Виталий
 */
public class MYSQLConnect {

    private static final SessionFactory sessionFactory;
    
    final static StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
            .build();
    
    static {

        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static synchronized SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    
}
