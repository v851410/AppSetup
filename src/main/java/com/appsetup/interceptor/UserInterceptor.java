package com.appsetup.interceptor;

import com.appsetup.db.dao.UserDAO;
import com.appsetup.db.entity.User;
import com.appsetup.srv.Roles;
import com.appsetup.type.UserRole;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Виталий
 */
public class UserInterceptor  extends HandlerInterceptorAdapter {

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    private UserDAO userDAO;
    
    public void setAccess(Roles access) {
        this.access = access;
    }

    @Autowired
    private Roles access;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        //Общая проверка всех запросов к /users
        if (!access.hasRoles(SecurityContextHolder.getContext().getAuthentication().getAuthorities(), UserRole.ADMIN, UserRole.ADOPS)) {
            request.setAttribute("messError", "true");
            request.setAttribute("typeError", "access_denied");
            request.getServletContext().getRequestDispatcher("/printErrot").forward(request, response);
            return false;
        }
        
        HttpSession session = request.getSession();
        UserRole role = (UserRole) session.getAttribute("role");

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
      
        RequestMapping rm = ((HandlerMethod) object).getMethodAnnotation(
                            RequestMapping.class);

        String operation = rm.value()[0];

        UserRole extr = UserRole.ANONYMOUS;

        if ("/update/{id}/".equals(operation)
                || "/delete/".equals(operation)){
            User user = userDAO.getUserByID(Integer.parseInt(pathVariables.get("id")));
            extr = user.getRole();
        } else if ("/add/".equals(operation)) {
            extr = UserRole.valueOf(request.getParameter("role"));
        }

        if ("/update/{id}/".equals(operation)
                || "/add/".equals(operation)) {
            
            //Действие над PUBLISHER (админ и оператор)
            if (extr == UserRole.PUBLISHER
                    && (role == UserRole.ADMIN || role == UserRole.ADOPS)) {
                return true;
            }
            //Действие над ADMIN (только админ)
            if (extr == UserRole.ADMIN
                    && role == UserRole.ADMIN) {
                return true;
            }
            //Действие над ADOPS (только админ)
            if (extr == UserRole.ADOPS
                    && role == UserRole.ADMIN) {
                return true;
            }

        } else if ("/delete/".equals(operation)) {
            
            if (extr == UserRole.PUBLISHER
                    && (role == UserRole.ADOPS || role == UserRole.ADMIN)) {
                return true;
            }
            if (extr == UserRole.ADOPS
                    && role == UserRole.ADMIN) {
                return true;
            }
            if (extr == UserRole.ADMIN
                    && role == UserRole.ADMIN) {
                return true;
            }
            
            
        }  else {
             return true;
        }
            request.setAttribute("messError", "current:"+role+" extr:"+extr);
            request.setAttribute("typeError", "access_denied");
            request.getServletContext().getRequestDispatcher("/printErrot").forward(request, response);

        return false;
    }
}
