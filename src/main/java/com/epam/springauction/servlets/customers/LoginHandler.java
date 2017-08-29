/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.customers;

import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.factories.Factory;
import com.epam.springauction.factories.OracleFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mikhail_Bobriashov
 */
public class LoginHandler extends HttpServlet {

    Factory oracleFactory = null;
    CustomerDAO customersManager = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            OracleFactory.createConnection();
            customersManager.getConnection();
            if (customersManager.checkCustomer(login, password)) {
                HttpSession session = request.getSession();
                // если сессия не новая, то уничтожаем ее и создаем новую
                if (!session.isNew()) {
                    session.invalidate();
                    session = request.getSession(true);
                    session.setAttribute("login", login);
                } else {
                    session.setAttribute("login", login);
                }

            } else {
                RequestDispatcher view = request.getRequestDispatcher("/pages/errorpages/loginerror.html");
                view.forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                // закрываем соединение
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        RequestDispatcher view = request.getRequestDispatcher("/serv/showitems");
        view.forward(request, response);
    }

    @Override
    public void init() throws ServletException {
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE);
        customersManager = oracleFactory.getCustomerDAO();
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
