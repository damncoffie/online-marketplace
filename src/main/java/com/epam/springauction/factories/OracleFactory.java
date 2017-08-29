package com.epam.springauction.factories;

import com.epam.springauction.oracleDBManagers.OracleBidManager;
import com.epam.springauction.oracleDBManagers.OracleCustomerManager;
import com.epam.springauction.oracleDBManagers.OracleItemManager;
import com.epam.springauction.daoInterfaces.BidDAO;
import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.daoInterfaces.ItemDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by Cooper on 03.07.2017
 */
public class OracleFactory extends Factory {

    //public static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    //public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    //private static final String USER = "market_user";
    //private static final String PASS = "000";
    static private Connection con = null;

    public static void createConnection() throws SQLException, ClassNotFoundException, NamingException {
        // без этого вылетают исключения:
        // ORA-00604: error occurred at recursive SQL level 1
        // ORA-12705: Cannot access NLS data files or invalid environment specified
        Locale.setDefault(Locale.ENGLISH);

        Logger.getLogger(OracleFactory.class.getName()).log(Level.INFO, "Connection to database...");
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/marketDB");
        con = ds.getConnection();
        Logger.getLogger(OracleFactory.class.getName()).log(Level.INFO, "Connection is ok");
    }

    public static void closeConnection() throws SQLException {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(OracleFactory.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Connection getConnection() {
        return con;
    }

    @Override
    public CustomerDAO getCustomerDAO() {
        return new OracleCustomerManager();
    }

    @Override
    public ItemDAO getItemDAO() {
        return new OracleItemManager();
    }

    @Override
    public BidDAO getBidDAO() {
        return new OracleBidManager();
    }

}
