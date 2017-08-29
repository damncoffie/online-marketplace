package com.epam.springauction.daoInterfaces;

import com.epam.springauction.entities.Bid;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Cooper on 03.07.2017.
 */
public interface BidDAO {

    ArrayList<Bid> getBidsByItemID(int itemID) throws SQLException;

    int createBid(Bid newBid) throws SQLException;

    Bid getHighestBid(int itemID) throws SQLException;

    void getConnection();

}
