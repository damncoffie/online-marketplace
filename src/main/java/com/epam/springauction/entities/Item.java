package com.epam.springauction.entities;

import java.io.Serializable;
import java.util.Objects;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.Size;

/**
 * Created by Cooper on 03.07.2017
 */
public class Item implements Serializable {

    private int itemID;
    //@Min(value = 4, message = "Seller id should be greater than 0")
    private int sellerID;
    //@Size(min = 4, max = 15, message = "Title should consist of from 1 to 15 symblos")
    private String title;
    private String description;
    //@Min(value = 4, message = "Start price id should be greater than 0")
    private int startPrice;
    private int bidIncrement;
    private String finishDate;
    private int buyItNow;
    private int isSold;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false; // проверка на NullPointer
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Item)) {
            return false;  //   проверка на класс
        }
        Item other = (Item) obj;
        if (other.title == null
                || other.description == null
                || other.startPrice == 0 || other.finishDate == null
                || other.bidIncrement == 0) {
            return false;
        }

        return other.title.equals(this.title)
                && other.description.equals(this.description)
                && other.startPrice == this.startPrice
                && other.finishDate.equals(this.finishDate)
                && other.bidIncrement == this.bidIncrement
                && other.sellerID == this.sellerID
                && other.buyItNow == this.buyItNow
                && other.finishDate.equals(this.finishDate);
    }

    @Override
    public int hashCode() {
        // генерим хешкод на основе полей
        // Warning: When a single object reference is supplied, the returned value does not equal the hash code of
        // that object reference. (из JavaDoc)
        return Objects.hash(sellerID, title, description, startPrice, finishDate, finishDate, buyItNow,
                bidIncrement);
    }

    @Override
    public String toString() {
        return "Title: " + this.title + " /Description: " + this.description
                + " /Start price: " + this.startPrice + " /Bid inc: " + this.bidIncrement;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public int getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(int bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public int getBuyItNow() {
        return buyItNow;
    }

    public void setBuyItNow(int buyItNow) {
        this.buyItNow = buyItNow;
    }

    public int getIsSold() {
        return isSold;
    }

    public void setIsSold(int isSold) {
        this.isSold = isSold;
    }

}
