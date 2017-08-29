/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.ajax;

import com.epam.springauction.daoInterfaces.BidDAO;
import com.epam.springauction.daoInterfaces.ItemDAO;
import com.epam.springauction.entities.Bid;
import com.epam.springauction.entities.Item;
import com.epam.springauction.factories.Factory;
import com.epam.springauction.factories.OracleFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mikhail_Bobriashov
 */
public class AjaxHandler extends HttpServlet {

    Factory oracleFactory = null;
    ItemDAO itemsManager = null;
    BidDAO bidsManager = null;

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
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String type = (String) session.getAttribute("type");

        try {
            // открываем соединение с бд
            OracleFactory.createConnection();
            bidsManager.getConnection();
            itemsManager.getConnection();
            // получаем товары
            ArrayList<Item> items = getItems(type, session);
            // получаем все высшие ставки на них
            ArrayList<Bid> highestBids = new ArrayList();
            for (Item item : items) {
                Bid bid = bidsManager.getHighestBid(item.getItemID());
                highestBids.add(bid);
            }
            // готовим объект, который станет json'ом
            ArrayList[] lists = new ArrayList[2];
            lists[0] = items;
            lists[1] = highestBids;

            // создаем и отсылаем json
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String jsonString = gson.toJson(lists);
            out.println(jsonString);

        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(AjaxHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            // закрываем соединение
            out.close();
            try {
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(AjaxHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private ArrayList<Item> getItems(String type, HttpSession session) throws SQLException {
        ArrayList<Item> items = null;
        /*
            определяем, какого типа товары нужны - 4 типа:
            1. all - все товары(для гостя);
            2. search - товары по поиску (нужен тип поиска и ключевые слова);
            3. mine - только мои (нужен логин);
            4. others - все товары, кроме моих (нужен айди)
         */
        switch (type) {
            case "all":// 1
                items = itemsManager.getAllItems();
                break;
            case "search":// 2
                String searchKey = (String) session.getAttribute("searchkey");
                String searchType = (String) session.getAttribute("searchtype");
                switch (searchType) {
                    case "Title":
                        items = itemsManager.getItemsByTitleSubstr(searchKey);
                        break;
                    case "Seller":
                        items = itemsManager.getItemsBySellerSubstr(searchKey);
                        break;
                }
                break;
            case "mine":// 3
                String login = (String) session.getAttribute("login");
                items = itemsManager.getItemsByLogin(login);
            case "others":
                int id = (Integer) session.getAttribute("id");
                items = itemsManager.getAllOthersItems(id);
        }
        return items;
    }

    @Override
    public void init() throws ServletException {
        // создаем дао-фабрику и инициализируем дао-объекты
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE);
        itemsManager = oracleFactory.getItemDAO();
        bidsManager = oracleFactory.getBidDAO();
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
