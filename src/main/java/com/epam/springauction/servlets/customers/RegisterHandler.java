/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.customers;

import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.entities.Customer;
import com.epam.springauction.factories.Factory;
import com.epam.springauction.factories.OracleFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mikhail_Bobriashov
 */
@WebServlet(name = "RegisterHandler", urlPatterns = {"/serv/register"})
public class RegisterHandler extends HttpServlet {

    Factory oracleFactory = null;
    CustomerDAO customerManager = null;

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

    }

    @Override
    public void init() throws ServletException {
        // создаем дао-фабрику и инициализируем дао-объекты
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE);
        customerManager = oracleFactory.getCustomerDAO();
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
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(request.getParameter("firstname"));
        newCustomer.setLastName(request.getParameter("lastname"));
        newCustomer.setBillingAddress(request.getParameter("billingaddress"));
        newCustomer.setLogin(request.getParameter("login"));
        newCustomer.setPassword(request.getParameter("password"));

        try {
            // открываем соединение с бд
            OracleFactory.createConnection();
            customerManager.getConnection();
            customerManager.addNewCustomer(newCustomer);
        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(RegisterHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                // закрываем соединение с бд
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(RegisterHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        Logger.getLogger(RegisterHandler.class.getName()).log(Level.INFO, "New customer was successfully added: \n",
                newCustomer.toString());
        request.getSession().setAttribute("login", request.getParameter("login"));
        RequestDispatcher view = request.getRequestDispatcher("/serv/showitems");
        view.forward(request, response);
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Register Handler";
    }// </editor-fold>

}
