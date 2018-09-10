package com.appsetup.service;

import com.appsetup.db.dao.UserDAO;
import com.appsetup.db.entity.User;
import com.appsetup.srv.Roles;
import com.appsetup.type.UserRole;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users/")
public class UserServices {
    
    @Autowired
    UserDAO userDAO;

    @Autowired
    Roles access;
    
    //http://localhost:8080/users/add/?name=NAME_SERVLET&password=112233&email=email@com&role=ADMIN
    @RequestMapping(value = "/add/" ,produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> create(
            @RequestParam(required = true, value = "name") String name,
            @RequestParam(required = true, value = "password") String password,
            @RequestParam(required = true, value = "email") String email,
            @RequestParam(required = true, value = "role") UserRole role,
            Authentication authentication,
            HttpSession session
    ) throws Exception {

        if ((UserRole) session.getAttribute("role") == UserRole.ADOPS
                && (role == UserRole.ADMIN || role == UserRole.PUBLISHER)) {
            Map ans = new HashMap();
            ans.put("error", "Ваша роль запрещает создавать пользователя с ролью "+role);
            return new ResponseEntity<>(ans, HttpStatus.OK);
        }
        
        User user = userDAO.createUser(name, email, password, role);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    //http://localhost:8080/users/get/name/VitaliyBrian
    @RequestMapping(value = "/get/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> getByName(HttpSession session,
            @PathVariable("name") String name) {

        User user = userDAO.getUserByLogin(name);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    //http://localhost:8080/users/find/name/Vitaliy
    @RequestMapping(value = "/find/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> findByName(HttpSession session,
            @PathVariable("name") String name) {

        List<User> user = userDAO.findUserByName(name);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    //http://localhost:8080/users/update/?id=14&name=userNewUpdate&password=112233&email=email@com&role=ADMIN
    @RequestMapping(value = "/update/{id}/" ,produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> update(
            @PathVariable("id") int id,
            @RequestParam(required = false, value = "name") String name,
            @RequestParam(required = false, value = "password") String password,
            @RequestParam(required = false, value = "email") String email,
            @RequestParam(required = false, value = "role") String role,
            Authentication authentication,
            HttpSession session
    ) throws Exception {

        User user = userDAO.updateUser(id, name, email, password, role);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }    
    
    @RequestMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> delete(
            @PathVariable("id") int id) throws Exception {
        userDAO.deleteByID(id);
        Map rez = new HashMap();
        rez.put("delete", "success");
        return new ResponseEntity<>(rez, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/test/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> test() throws Exception {
        
        User user = userDAO.getUserByID(41);
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    
    @RequestMapping(value = "/byId/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    ResponseEntity<Object> getById(@PathVariable("id") int id) throws Exception {
        
        User user = userDAO.getUserByID(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
