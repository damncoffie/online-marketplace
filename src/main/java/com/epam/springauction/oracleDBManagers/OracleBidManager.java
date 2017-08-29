package com.epam.springauction.oracleDBManagers;

import com.epam.springauction.daoInterfaces.BidDAO;
import com.epam.springauction.entities.Bid;
import com.epam.springauction.factories.OracleFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Cooper on 03.07.2017
 */
public class OracleBidManager implements BidDAO {

    private Connection con;
    private PreparedStatement pStatement;

    public void getConnection() {
        con = OracleFactory.getConnection();
    }

    @Override
    public ArrayList<Bid> getBidsByItemID(int itemID) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM BIDS WHERE item_id = ?");
        pStatement.setInt(1, itemID);
        ResultSet rs = pStatement.executeQuery();

        if (!rs.isAfterLast()) {
            ArrayList<Bid> bids = new ArrayList<>();
            while (rs.next()) {
                Bid bid = getBidWithData(rs);
                bids.add(bid);
            }
            return bids;
        } else {
            return null;
        }
    }

    @Override
    public int createBid(Bid newBid) throws SQLException {
        pStatement = con.prepareStatement("INSERT INTO BIDS (bidder_id, item_id, bid) "
                + "VALUES (?, ?, ?)");
        pStatement.setInt(1, newBid.getBidder_id());
        pStatement.setInt(2, newBid.getItem_id());
        pStatement.setInt(3, newBid.getBid());
        // 0 - если не было изменений, в ином случае - количество измененных строк (тут: 1)
        return pStatement.executeUpdate();
    }

    @Override
    public Bid getHighestBid(int itemID) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM ("
                + "SELECT bid_id, bidder_id, item_id, max(bid) bid "
                + "FROM BIDS "
                + "WHERE item_id = ? "
                + "GROUP BY bid_id, bidder_id, item_id "
                + "ORDER BY bid DESC) "
                + "WHERE ROWNUM = 1");
        pStatement.setInt(1, itemID);
        ResultSet rs = pStatement.executeQuery();
        rs.next();

        if (!rs.isAfterLast()) {
            return getBidWithData(rs);
        } else {
            return null;
        }
    }

    private Bid getBidWithData(ResultSet rs) throws SQLException {
        Bid bid = new Bid();
        bid.setBid_id(rs.getInt(1));
        bid.setBidder_id(rs.getInt(2));
        bid.setItem_id(rs.getInt(3));
        bid.setBid(rs.getInt(4));

        return bid;
    }

}
