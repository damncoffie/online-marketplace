package com.epam.springauction.oracleDBManagers;

import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.entities.Customer;
import com.epam.springauction.factories.OracleFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Created by Cooper on 03.07.2017
 */
@Component
public class OracleCustomerManager implements CustomerDAO {

    private Connection con;
    private PreparedStatement pStatement;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // method for spring check!!!
    @Override
    public boolean springCheckCustomer(String login, String password) {
        String sql = "SELECT * FROM USERS WHERE login = ? AND password = ?";
        List<String> list = jdbcTemplate.query(
                sql,
                new Object[]{login, password},
                new RowMapper() {
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        }
        );

        return !list.isEmpty();
    }

    @Override
    public void getConnection() {
        con = OracleFactory.getConnection();
    }

    @Override
    public ArrayList<Customer> getAllCustomers() throws SQLException {

        pStatement = con.prepareStatement("SELECT * FROM USERS");
        ResultSet rs = pStatement.executeQuery();
        ArrayList<Customer> allCustomers = new ArrayList<>();
        while (rs.next()) {
            Customer customer = fillCustomerWithData(rs);
            allCustomers.add(customer);
        }
        return allCustomers;
    }

    @Override
    public int addNewCustomer(Customer newCustomer) throws SQLException {
        pStatement = con.prepareStatement("INSERT INTO USERS (first_name, last_name, billing_address, login, password)"
                + " VALUES (?, ?, ?, ?, ?)");
        fillCustomerPStatementValues(pStatement, newCustomer);
        // 0 - если не было изменений, в ином случае - количество измененных строк (тут: 1)
        return pStatement.executeUpdate();
    }

    @Override
    public Customer getCustomerByLogin(String login) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM USERS WHERE LOGIN = ?");
        pStatement.setString(1, login);
        ResultSet rs = pStatement.executeQuery();
        rs.next();

        if (!rs.isAfterLast()) {
            return fillCustomerWithData(rs);
        } else {
            return null;
        }
    }

    @Override
    public int changeCustomer(String oldCustomerLogin, Customer changedCustomer) throws SQLException {
        pStatement = con.prepareStatement("UPDATE USERS SET first_name = ?, last_name = ?, billing_address = ?,"
                + "login = ?, password = ? WHERE login = ?");
        fillCustomerPStatementValues(pStatement, changedCustomer);
        pStatement.setString(6, oldCustomerLogin);
        // 0 - если не было изменений, в ином случае - количество измененных строк (тут: 1)
        return pStatement.executeUpdate();
    }

    // new
    @Override
    public boolean checkCustomer(String login, String password) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM USERS WHERE login = ? AND password = ?");
        pStatement.setString(1, login);
        pStatement.setString(2, password);
        ResultSet rs = pStatement.executeQuery();
        return rs.next();
    }

    private Customer fillCustomerWithData(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setUserID(rs.getInt(1));
        customer.setFirstName(rs.getString(2));
        customer.setLastName(rs.getString(3));
        customer.setBillingAddress(rs.getString(4));
        customer.setLogin(rs.getString(5));
        customer.setPassword(rs.getString(6));

        return customer;
    }

    private void fillCustomerPStatementValues(PreparedStatement pStatement, Customer customer) throws SQLException {
        pStatement.setString(1, customer.getFirstName());
        pStatement.setString(2, customer.getLastName());
        pStatement.setString(3, customer.getBillingAddress());
        pStatement.setString(4, customer.getLogin());
        pStatement.setString(5, customer.getPassword());
    }

}
