package com.appsetup.interceptor;

import com.appsetup.db.dao.UserDAO;
import com.appsetup.db.entity.User;
import com.appsetup.srv.Roles;
import com.appsetup.type.UserRole;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Виталий
 */
public class SessionInterceptor extends HandlerInterceptorAdapter  {
    
    public void setAccess(Roles access) {
        this.access = access;
    }
    
   public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

   @Autowired
    private UserDAO userDAO;    

    @Autowired
    private Roles access;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        HttpSession session = request.getSession();
        
        if (isUserLogged() 
                && (session.getAttribute("login") == null 
                    || session.getAttribute("role") == null) ) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            session.setAttribute("login", authentication.getName());
            GrantedAuthority auth = (GrantedAuthority) authentication.getAuthorities().toArray()[0];
            session.setAttribute("role", UserRole.valueOf(auth.getAuthority()));
            User user = userDAO.getUserByLogin(session.getAttribute("login").toString());
            session.setAttribute("UserId", String.valueOf(user.getId()));
        }

        return true;

    }
    
    public static boolean isUserLogged() {
        try {
            return !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            return false;
        }
    }
    
}
