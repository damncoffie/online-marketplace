package com.epam.springauction.oracleDBManagers;

import com.epam.springauction.daoInterfaces.ItemDAO;
import com.epam.springauction.entities.Item;
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
// аннотация для автовайринга. имплементация ItemDAO всего одна, так что хватит
// @Component. если было бы несколько имплементаций, надо было бы использовать
// @Qualifier(value = name)
@Component
public class OracleItemManager implements ItemDAO {

    private Connection con;
    private PreparedStatement pStatement;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // method for spring search!!!
    @Override
    public List advanceFind(Item item) {
        String sql = "SELECT * FROM ITEMS WHERE seller_id = ? OR title = ? OR start_price = ?";
        List<Item> items = jdbcTemplate.query(
                sql,
                new Object[]{item.getSellerID(), item.getTitle(), item.getStartPrice()},
                new RowMapper<Item>() {
            @Override
            public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                Item newItem = getItemWithData(rs);

                return newItem;
            }
        });
        return items;
    }

    @Override
    public void getConnection() {
        con = OracleFactory.getConnection();
    }

    @Override
    public ArrayList<Item> getAllItems() throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM ITEMS ORDER BY item_id DESC");
        ResultSet rs = pStatement.executeQuery();
        ArrayList<Item> allItems = new ArrayList<>();

        while (rs.next()) {
            Item item = getItemWithData(rs);
            allItems.add(item);
        }
        return allItems;
    }

    @Override
    public ArrayList<Item> getAllOthersItems(int myID) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM ITEMS WHERE SELLER_ID NOT IN (?) ORDER BY item_id DESC");
        pStatement.setInt(1, myID);
        ResultSet rs = pStatement.executeQuery();
        ArrayList<Item> allItems = new ArrayList<>();

        while (rs.next()) {
            Item item = getItemWithData(rs);
            allItems.add(item);
        }
        return allItems;
    }

    @Override
    public ArrayList<Item> getItemsByTitleSubstr(String subString) throws SQLException {

        pStatement = con.prepareStatement("SELECT * FROM ITEMS WHERE TITLE LIKE ?");
        pStatement.setString(1, "%" + subString + "%");
        ResultSet rs = pStatement.executeQuery();

        if (!rs.isAfterLast()) {
            ArrayList<Item> allAlikeItems = new ArrayList<>();
            while (rs.next()) {
                Item item = getItemWithData(rs);
                allAlikeItems.add(item);
            }
            return allAlikeItems;
        } else {
            return null;
        }
    }

    @Override
    public Item getItemByID(int id) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM ITEMS WHERE item_id = ?");
        pStatement.setInt(1, id);
        ResultSet rs = pStatement.executeQuery();
        rs.next();

        if (!rs.isAfterLast()) {
            return getItemWithData(rs);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Item> getItemsBySellerSubstr(String subStringSeller) throws SQLException {

        pStatement = con.prepareStatement("SELECT item_id, seller_id, title, description, start_price, "
                + "bid_increment, finish_date, buy_it_now, is_sold "
                + "FROM ITEMS, USERS "
                + "WHERE ITEMS.SELLER_ID = USERS.USER_ID "
                + "AND (FIRST_NAME LIKE ? OR LAST_NAME LIKE ?)");
        pStatement.setString(1, "%" + subStringSeller + "%");
        pStatement.setString(2, "%" + subStringSeller + "%");
        ResultSet rs = pStatement.executeQuery();

        if (!rs.isAfterLast()) {
            ArrayList<Item> allSellersItems = new ArrayList<>();
            while (rs.next()) {
                Item item = getItemWithData(rs);
                allSellersItems.add(item);
            }
            return allSellersItems;
        } else {
            return null;
        }
    }

    // changed
    @Override
    public int addNewItem(Item newItem) throws SQLException {
        pStatement = con.prepareStatement("INSERT INTO ITEMS (seller_id, title, description, start_price, bid_increment, "
                + "finish_date, buy_it_now, is_sold) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        pStatement.setInt(1, newItem.getSellerID());
        pStatement.setString(2, newItem.getTitle());
        pStatement.setString(3, newItem.getDescription());
        pStatement.setInt(4, newItem.getStartPrice());
        pStatement.setInt(5, newItem.getBidIncrement());
        pStatement.setString(6, newItem.getFinishDate());
        pStatement.setInt(7, newItem.getBuyItNow());
        pStatement.setInt(8, newItem.getIsSold());
        // 0 - если не было изменений, в ином случае - количество измененных строк (тут: 1)
        System.out.println("From DAO!" + "\n" + "Title: " + newItem.getTitle() + "\n"
                + "Description: " + newItem.getDescription() + "\n"
                + "Start price: " + newItem.getStartPrice() + "\n"
                + "Buy it now: " + newItem.getBuyItNow() + "\n"
                + "Finish date: " + newItem.getFinishDate() + "\n"
                + "Increment:  " + newItem.getBidIncrement());
        return pStatement.executeUpdate();
    }

    @Override
    // С помощью sql-скрипта создать в базе товар с определённым характеристиками.
    // Вызвать метод для получения товара по указанному UID. Вызвать метод продажи товара.
    public int sellItem(int itemID) throws SQLException {
        pStatement = con.prepareStatement("UPDATE ITEMS SET is_sold = 1 WHERE item_id = ?");
        pStatement.setInt(1, itemID);
        // 0 - если не было изменений, в ином случае - количество измененных строк (тут: 1)
        return pStatement.executeUpdate();
    }

    // new
    public int changeItem(int itemID, Item newItem) throws SQLException {
        pStatement = con.prepareStatement("UPDATE items SET title = ?, description = ?, start_price = ?,"
                + "buy_it_now = ?, finish_date = ?, bid_increment = ? WHERE item_id = ?");
        pStatement.setString(1, newItem.getTitle());
        pStatement.setString(2, newItem.getDescription());
        pStatement.setInt(3, newItem.getStartPrice());
        pStatement.setInt(4, newItem.getBuyItNow());
        pStatement.setString(5, newItem.getFinishDate());
        pStatement.setInt(6, newItem.getBidIncrement());
        pStatement.setInt(7, itemID);
        // 0 - если не было изменений, в ином случае - количество измененных строк (тут: 1)
        return pStatement.executeUpdate();
    }

    // new
    @Override
    public ArrayList<Item> getItemsByLogin(String login) throws SQLException {
        pStatement = con.prepareStatement("SELECT * FROM items "
                + "WHERE SELLER_ID = (select USER_ID from users "
                + "where login = ?)");
        pStatement.setString(1, login);
        ResultSet rs = pStatement.executeQuery();
        ArrayList<Item> itemsByLogin = new ArrayList<>();

        while (rs.next()) {
            Item item = getItemWithData(rs);
            itemsByLogin.add(item);
        }
        return itemsByLogin;
    }

    private Item getItemWithData(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemID(rs.getInt(1));
        item.setSellerID(rs.getInt(2));
        item.setTitle(rs.getString(3));
        item.setDescription(rs.getString(4));
        item.setStartPrice(rs.getInt(5));
        item.setBidIncrement(rs.getInt(6));
        item.setFinishDate(rs.getString(7).replace(":00.0", ""));
        item.setBuyItNow(rs.getInt(8));
        item.setIsSold(rs.getInt(9));

        return item;
    }

}
