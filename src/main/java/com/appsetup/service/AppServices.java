package com.appsetup.service;

import com.appsetup.db.dao.AppDAO;
import com.appsetup.db.dao.UserDAO;
import com.appsetup.db.entity.App;
import com.appsetup.db.entity.User;
import com.appsetup.type.AppType;
import com.appsetup.type.ContentType;
import com.appsetup.type.UserRole;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app/")
public class AppServices {

    @Autowired
    AppDAO appDAO;
    
    @Autowired
    UserDAO userDAO; 
    
    //Создание нового приложения
    //http://localhost:8080/app/create/?name=APP_NEW&type=IOS
    @RequestMapping(value = "/create/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> create(
            @RequestParam(required = true, value = "name") String name,
            @RequestParam(required = true, value = "type") AppType appType,
            @RequestParam(required = false, value = "userID") String userID,
            HttpSession session
    ) throws Exception {
        
        UserRole role = (UserRole) session.getAttribute("role");
        if (role == UserRole.PUBLISHER) {
            userID = session.getAttribute("UserId").toString();
        } else if (Strings.nullToEmpty(userID).isEmpty()) {
            throw new Exception("Set user id");
        }       

        App app = appDAO.create(name, appType, userID);
        return new ResponseEntity<>(app, HttpStatus.OK);
    }

    //Редактирование приложения
    //http://localhost:8080/app/update/?id=9&name=APP_NEW&type=ANDROID
    @RequestMapping(value = "/update/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> update(
            @RequestParam(required = true, value = "id") int id,
            @RequestParam(required = true, value = "name") String name,
            @RequestParam(required = true, value = "type") AppType appType,
            @RequestParam(required = true, value = "userID") String userID,
            HttpSession session
    ) throws Exception {

        App app = appDAO.update(id, name, appType, userID);
        return new ResponseEntity<>(app, HttpStatus.OK);
    }
    
    //Удаление приложения
    //http://localhost:8080/app/delete/?id=5
    @RequestMapping(value = "/delete/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> delete(
            @RequestParam(required = true, value = "id") int id) throws Exception {
        appDAO.deleteByID(id);
        Map rez = new HashMap();
        rez.put("delete", "success");
        return new ResponseEntity<>(rez, HttpStatus.OK);
    }    

    @RequestMapping(value = "/find/findAppByUser/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> findAppByUser(
            @RequestParam(required = false, value = "id") String userID, HttpSession session) throws Exception {

        UserRole role = (UserRole) session.getAttribute("role");
        if (role == UserRole.PUBLISHER) {
            userID = session.getAttribute("UserId").toString();
        }

        List<App> appLs = appDAO.findAppList(userID);
        return new ResponseEntity<>(appLs, HttpStatus.OK);
    }    
    
    //Создание контента
    //http://localhost:8080/app/content/create/?type=IMAGE&app_id=5
    @RequestMapping(value = "/content/create/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> createContent(
            @RequestParam(required = true, value = "type") ContentType type,
            @RequestParam(required = true, value = "app_id") int app_id) throws Exception {

        App app = appDAO.createContent(type, app_id);
        return new ResponseEntity<>(app, HttpStatus.OK);
    }   
    

    @RequestMapping(value = "/byID/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> findByID(@PathVariable("id") int id) throws Exception {

        App app = appDAO.getAppByID(id);

        return new ResponseEntity<>(app, HttpStatus.OK);
    }

    @RequestMapping(value = "/content/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> contentDelete(@PathVariable("id") int id) throws Exception {

        appDAO.deleteContent(id);
        Map rez = new HashMap();
        rez.put("delete", "success");
        return new ResponseEntity<>(rez, HttpStatus.OK);
    }
    
    
}
