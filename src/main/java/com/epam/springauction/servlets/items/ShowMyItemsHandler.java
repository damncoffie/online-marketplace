/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.items;

import com.epam.springauction.daoInterfaces.ItemDAO;
import com.epam.springauction.entities.Item;
import com.epam.springauction.factories.Factory;
import com.epam.springauction.factories.OracleFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class ShowMyItemsHandler extends HttpServlet {

    Factory oracleFactory = null;
    ItemDAO itemsManager = null;

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
        String login = (String) request.getSession().getAttribute("login");
        try {
            // открываем соединение с бд
            OracleFactory.createConnection();
            itemsManager.getConnection();
            ArrayList<Item> customerItems = itemsManager.getItemsByLogin(login);
            request.setAttribute("myitems", customerItems);

            // для AJAX-запросов
            HttpSession session = request.getSession();
            session.setAttribute("type", "mine");// если my, то в ajax-запрос надо добавить атрибут login из сессии(логика в js)

        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(ShowMyItemsHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                // закрываем соединение с бд
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(ShowMyItemsHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        RequestDispatcher view = request.getRequestDispatcher("/pages/showmyitems.jsp");
        view.forward(request, response);
    }

    @Override
    public void init() throws ServletException {
        // создаем дао-фабрику и инициализируем дао-объекты
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE);
        itemsManager = oracleFactory.getItemDAO();
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
