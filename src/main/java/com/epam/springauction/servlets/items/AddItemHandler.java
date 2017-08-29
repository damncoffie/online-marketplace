/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.servlets.items;

import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.daoInterfaces.ItemDAO;
import com.epam.springauction.entities.Customer;
import com.epam.springauction.entities.Item;
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
public class AddItemHandler extends HttpServlet {

    Factory oracleFactory = null;
    ItemDAO itemsManager = null;
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
        oracleFactory = Factory.getDAOFactory(Factory.ORACLE); // инициализируем дао-фабрику
        itemsManager = oracleFactory.getItemDAO();
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
        // смотрим на тип запроса
        String whatToDo = request.getParameter("whattodo");
        try {
            // открываем соединение с бд
            OracleFactory.createConnection();
            customerManager.getConnection();
            itemsManager.getConnection();

            Item newItem = createNewItem(request);

            if (whatToDo.equals("add")) {
                Logger.getLogger(AddItemHandler.class.getName()).log(Level.INFO,
                        "New item was successfully added: \n" + newItem.toString());
                itemsManager.addNewItem(newItem);
            }
            if (whatToDo.equals("change")) {
                String paramItemID = request.getParameter("itemid");
                int itemID = Integer.parseInt(paramItemID);
                itemsManager.changeItem(itemID, newItem);
                Logger.getLogger(AddItemHandler.class.getName()).log(Level.INFO,
                        "New item was successfully changed: " + newItem.toString());
            }
        } catch (SQLException | ClassNotFoundException | NamingException e) {
            Logger.getLogger(AddItemHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                // закрываем соединение с бд
                OracleFactory.closeConnection();
            } catch (SQLException e) {
                Logger.getLogger(AddItemHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        // возвращаемся на предыдущуюю страницу с обновленным списком
        RequestDispatcher view = request.getRequestDispatcher("/serv/showmyitems");
        view.forward(request, response);
    }

    private Item createNewItem(HttpServletRequest request) {
        // берем логин из сессии
        String login = (String) request.getSession().getAttribute("login");

        // создаем новый айтем и заполням из атрибутов формы
        Item newItem = new Item();
        newItem.setTitle(request.getParameter("title"));
        newItem.setDescription(request.getParameter("description"));
        newItem.setStartPrice(Integer.parseInt(request.getParameter("startprice")));
        // проверка значения из чекбокса (либо нулл, либо нет)
        if (request.getParameter("buyitnow") == null) {
            newItem.setBuyItNow(0);
            newItem.setFinishDate(request.getParameter("finishdate"));
            newItem.setBidIncrement(Integer.parseInt(request.getParameter("increment")));
        } else {
            newItem.setBuyItNow(1);
            newItem.setFinishDate("1999-07-07T23:59:59");
            newItem.setBidIncrement(0);
        }
        // добавляем собственный айди
        try {
            Customer cus = customerManager.getCustomerByLogin(login);
            newItem.setSellerID(cus.getUserID());
            newItem.setIsSold(0);
        } catch (SQLException e) {
            Logger.getLogger(AddItemHandler.class.getName()).log(Level.SEVERE, null, e);
        }

        return newItem;
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
        return "Add Item Handler";
    }// </editor-fold>

}
