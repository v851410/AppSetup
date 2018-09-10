package com.appsetup.db.dao;

import com.appsetup.db.entity.App;
import com.appsetup.db.entity.Content;
import com.appsetup.db.entity.User;
import com.appsetup.type.AppType;
import com.appsetup.type.ContentType;
import com.google.common.base.Strings;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AppDAO {
    
    @Autowired
    private SessionFactory sessionFactory;

    /***
     * Создание нового приложения
     * @param name
     * @param appType
     * @param userId
     * @return
     * @throws Exception 
     */
    public App create(String name, AppType appType, String userId) throws Exception {
        
        App app = buildAppFromParams(name, appType, userId);
        sessionFactory.getCurrentSession().save(app);
        
        return app;
    }
    
    public App getAppByID(int id) {
        
        return sessionFactory.getCurrentSession().get(App.class, id);
    }

    public int deleteByID(int id) {
        App app = getAppByID(id);
        sessionFactory.getCurrentSession().delete(app);
        return 1;
    } 
    
    public int deleteContent(int id) {
        Content content = sessionFactory.getCurrentSession().get(Content.class, id);
        sessionFactory.getCurrentSession().delete(content);
        return 1;
    }
    
    public App update(int id, String name, AppType appType, String userId) throws Exception {

        App app = buildAppFromParams(name, appType, userId);
        app.setId(id);
        sessionFactory.getCurrentSession().update(app);

        return app;
    }
    
    public App createContent(ContentType contentType, int appID) throws Exception {
        Content content = new Content();
        content.setType(contentType);
        content.setApp(getAppByID(appID));
        sessionFactory.getCurrentSession().save(content);
        return getAppByID(appID);
    }
    
    public List<App> findAppList(String user_id) {

       List<App> results= sessionFactory.getCurrentSession()
               .createQuery("from App where user_id=:user_id")
               .setString("user_id", user_id).list();
        
        return results;
    }
    
    private App buildAppFromParams(String name, AppType appType, String userId) {
        App app = new App();
        if (!Strings.nullToEmpty(name).isEmpty()) {
            app.setName(name);
        }
        if (appType != null) {
            app.setType(appType);
        }
        setUser(app, userId);
        return app;
    }
    
    private void setUser(App app, String userId) {
        User user = (User) sessionFactory.getCurrentSession().get(User.class, Integer.parseInt(userId));
        app.setUser(user);
    }
     
}
