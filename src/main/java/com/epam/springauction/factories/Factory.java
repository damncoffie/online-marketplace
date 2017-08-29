package com.epam.springauction.factories;

import com.epam.springauction.daoInterfaces.BidDAO;
import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.daoInterfaces.ItemDAO;

/**
 * Created by Cooper on 03.07.2017
 */
public abstract class Factory {

    // Список типов DAO, поддерживаемых генератором
    public static final int ORACLE = 1;
    public static final int MYSQL = 2;
    public static final int SQLITE = 3;

    public abstract CustomerDAO getCustomerDAO();

    public abstract ItemDAO getItemDAO();

    public abstract BidDAO getBidDAO();

    public static Factory getDAOFactory(int whichFactory) {

        switch (whichFactory) {
            case ORACLE:
                return new OracleFactory();
            // реализации других источников данных, когда понадобятся
            case MYSQL:
                return null; //new MySQLDAOFactory();
            case SQLITE:
                return null; //new SQLiteDAOFactory();
            default:
                return null;
        }
    }

}
