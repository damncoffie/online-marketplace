package com.epam.springauction.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Cooper on 03.07.2017
 */
public class Bid implements Serializable {

    private int bid_id;
    private int bidder_id;
    private int item_id;
    private int bid;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false; // проверка на NullPointer
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Bid)) {
            return false;  //   проверка на класс
        }
        Bid other = (Bid) obj;
        if (other.bidder_id == 0
                || other.item_id == 0
                || other.bid == 0) {
            return false;
        }

        return other.bidder_id == this.bidder_id
                && other.item_id == this.item_id
                && other.bid == this.bid;
    }

    @Override
    public int hashCode() {
        // генерим хешкод на основе полей
        // Warning: When a single object reference is supplied, the returned value does not equal the hash code of
        // that object reference. (из JavaDoc)
        return Objects.hash(bidder_id, item_id, bid);
    }

    @Override
    public String toString() {
        return this.bid_id + "/ " + this.bidder_id + "/ " + this.item_id + "/ " + this.bid;
    }

    public int getBid_id() {
        return bid_id;
    }

    public void setBid_id(int bid_id) {
        this.bid_id = bid_id;
    }

    public int getBidder_id() {
        return bidder_id;
    }

    public void setBidder_id(int bidder_id) {
        this.bidder_id = bidder_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

}
