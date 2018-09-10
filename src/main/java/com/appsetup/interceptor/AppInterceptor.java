package com.appsetup.interceptor;

import com.appsetup.db.dao.AppDAO;
import com.appsetup.db.entity.App;
import com.appsetup.type.UserRole;
import com.google.common.base.Strings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AppInterceptor extends HandlerInterceptorAdapter {

    public void setAppDAO(AppDAO appDAO) {
        this.appDAO = appDAO;
    }
    
    @Autowired
    private AppDAO appDAO;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
    
        HttpSession session = request.getSession();
        UserRole role = (UserRole) session.getAttribute("role");
        String userID = session.getAttribute("UserId").toString();
        
        RequestMapping rm = ((HandlerMethod) object).getMethodAnnotation(
                RequestMapping.class);
        String operation = rm.value()[0];
        
        if (role == UserRole.ADMIN) {
            request.setAttribute("messError", "ADMIN");
            request.setAttribute("typeError", "access_denied");
            request.getServletContext().getRequestDispatcher("/printErrot").forward(request, response);
            return false;
        }
        
        if (role == UserRole.PUBLISHER 
                && ("/update/".equals(operation) || "/delete/".equals(operation))) {
            int extrID = 0;
            if (!Strings.nullToEmpty(request.getParameter("id")).isEmpty()) {
                extrID = Integer.valueOf(request.getParameter("id"));
            } else if (!Strings.nullToEmpty(request.getParameter("app_id")).isEmpty()) {
                extrID = Integer.valueOf(request.getParameter("app_id"));
            } else {
                request.setAttribute("messError", "not find APP ID");
                request.setAttribute("typeError", "access_denied");
                request.getServletContext().getRequestDispatcher("/printErrot").forward(request, response);
                return false;
            }
            
            App app = appDAO.getAppByID(extrID);
            if (app.getUser().getId() != Integer.parseInt(userID)) {
                request.setAttribute("messError", "Error access for APP. extr_user : " + app.getUser().getId() + " curr_user : " + userID);
                request.setAttribute("typeError", "access_denied");
                request.getServletContext().getRequestDispatcher("/printErrot").forward(request, response);
                return false;
            }
            
        }
        
        return true;
    }
    
}
