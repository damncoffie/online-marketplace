/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.spring.interceptor;

//import com.epam.servlets.customers.LoginHandler;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Mikhail_Bobriashov
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Logger.getLogger(AuthInterceptor.class.getName()).log(Level.INFO, "User attempt to login at: "
                + new Date().toString() + " with login: " + request.getParameter("login"));
        return true;
    }

}
