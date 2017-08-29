package com.epam.springauction.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Cooper on 03.07.2017
 */
public class Customer implements Serializable {

    private int userID;
    private String firstName;
    private String lastName;
    private String billingAddress;
    private String login;
    private String password;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false; // проверка на NullPointer
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;  //   проверка на класс
        }
        Customer other = (Customer) obj;
        if (other.firstName == null || other.lastName == null || other.billingAddress == null || other.login == null
                || other.password == null) {
            return false;
        }
        // login - UNIQUE-поле из таблицы USERS
        // совпадение гаранитрует равенство
        return other.login.equals(this.login);
    }

    @Override
    public int hashCode() {
        // генерим хешкод на основе полей
        // Warning: When a single object reference is supplied, the returned value does not equal the hash code of
        // that object reference. (из JavaDoc)
        return Objects.hash(firstName, lastName, login, password, billingAddress);
    }

    @Override
    public String toString() {
        return "F name: " + this.firstName + " L name: "
                + this.lastName + " Address: " + this.billingAddress + " Login: "
                + this.login + " Password: " + this.password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
