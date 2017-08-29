package com.epam.springauction.daoInterfaces;

import com.epam.springauction.entities.Item;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cooper on 03.07.2017
 */
public interface ItemDAO {

    ArrayList<Item> getAllItems() throws SQLException;

    ArrayList<Item> getItemsByTitleSubstr(String subString) throws SQLException;

    Item getItemByID(int id) throws SQLException;

    ArrayList<Item> getItemsBySellerSubstr(String subStringSeller) throws SQLException;

    int addNewItem(Item newItem) throws SQLException;

    int sellItem(int itemID) throws SQLException;

    public ArrayList<Item> getItemsByLogin(String login) throws SQLException;

    public int changeItem(int itemID, Item newItem) throws SQLException;

    public ArrayList<Item> getAllOthersItems(int myID) throws SQLException;

    public void getConnection();

    public List advanceFind(Item item);
}
