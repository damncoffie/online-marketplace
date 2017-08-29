/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.bids;

import com.epam.springauction.daoInterfaces.BidDAO;
import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.entities.Bid;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mikhail_Bobriashov
 */
public class BidHandler extends HttpServlet {

    Factory oracleFactory = null;
    BidDAO bidsManager = null;
    CustomerDAO customersManager = null;

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

        String login = (String) request.getSession().getAttribute("login");

        Bid newBid = new Bid();
        newBid.setBid(Integer.parseInt(request.getParameter("bid")));
        newBid.setItem_id(Integer.parseInt(request.getParameter("itemid")));
        try {
            // открываем соединение с бд
            OracleFactory.createConnection();
            customersManager.getConnection();
            bidsManager.getConnection();

            Customer cus = customersManager.getCustomerByLogin(login);
            newBid.setBidder_id(cus.getUserID());
            bidsManager.createBid(newBid);
            Logger.getLogger(BidHandler.class.getName()).log(Level.INFO, "Bid added");
        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(BidHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                // закрываем соединение
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(BidHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        RequestDispatcher view = request.getRequestDispatcher("/serv/showitems");
        view.forward(request, response);
    }

    @Override
    public void init() throws ServletException {
        // создаем дао-фабрику и инициализируем дао-объекты
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE);
        bidsManager = oracleFactory.getBidDAO();
        customersManager = oracleFactory.getCustomerDAO();
    }

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
        return "Short description";
    }// </editor-fold>

}
