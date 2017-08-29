package com.epam.springauction.daoInterfaces;

import com.epam.springauction.entities.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Cooper on 03.07.2017
 */
public interface CustomerDAO {

    ArrayList<Customer> getAllCustomers() throws SQLException;

    int addNewCustomer(Customer newCustomer) throws SQLException;

    Customer getCustomerByLogin(String login) throws SQLException;

    int changeCustomer(String oldCustomerLogin, Customer newCustomer) throws SQLException;

    boolean checkCustomer(String login, String password) throws SQLException;

    public void getConnection();

    public boolean springCheckCustomer(String login, String password);
}
