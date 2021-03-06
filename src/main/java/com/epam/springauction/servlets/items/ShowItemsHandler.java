/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.items;

import com.epam.springauction.daoInterfaces.BidDAO;
import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.daoInterfaces.ItemDAO;
import com.epam.springauction.entities.Bid;
import com.epam.springauction.entities.Customer;
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
public class ShowItemsHandler extends HttpServlet {

    Factory oracleFactory = null;
    ItemDAO itemsManager = null;
    BidDAO bidsManager = null;
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
        String login = (String) request.getSession().getAttribute("login");

        try {
            // открываем соединение с бд
            OracleFactory.createConnection();
            customersManager.getConnection();
            itemsManager.getConnection();
            bidsManager.getConnection();
            if (itemsManager != null) {
                // получаем все товары
                Customer cus = customersManager.getCustomerByLogin(login);
                ArrayList<Item> items = null;
                HttpSession session = request.getSession();

                if (login.equals("guest")) {
                    // а вдруг гости
                    items = itemsManager.getAllItems();
                    // для AJAX-запросов
                    session.setAttribute("type", "all");
                } else {
                    // не гости
                    items = itemsManager.getAllOthersItems(cus.getUserID());
                    // для AJAX-запросов
                    session.setAttribute("type", "others");
                    session.setAttribute("id", cus.getUserID());
                }
                // получаем все высшие ставки на них
                ArrayList<Bid> highestBids = new ArrayList();
                for (Item item : items) {
                    Bid bid = bidsManager.getHighestBid(item.getItemID());
                    highestBids.add(bid);
                }
                // добавляем листы в атрибуты
                request.setAttribute("allitems", items);// добавляем атрибут - коллекцию всех товаров - в запрос
                request.setAttribute("allbids", highestBids);
            }
        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(ShowItemsHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                // закрываем соединение с бд
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(ShowItemsHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        // диспатчим дальше
        RequestDispatcher view = request.getRequestDispatcher("/pages/showitems.jsp");
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

    @Override
    public void init() throws ServletException {
        // создаем дао-фабрику и инициализируем дао-объекты
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE);
        itemsManager = oracleFactory.getItemDAO();
        bidsManager = oracleFactory.getBidDAO();
        customersManager = oracleFactory.getCustomerDAO();
    }

}
