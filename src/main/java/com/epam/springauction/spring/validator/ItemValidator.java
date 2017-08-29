/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epam.springauction.spring.validator;

import com.epam.springauction.entities.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author Mikhail_Bobriashov
 */
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class type) {
        return Item.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Item item = (Item) o;
        if (item.getSellerID() <= 0) {
            errors.rejectValue("sellerID", "negativeValue", new Object[]{"'sellerID'"}, "Seller id should be greater than 0");
        }
        if (item.getStartPrice() <= 0) {
            errors.rejectValue("startPrice", "negativeValue", new Object[]{"'startPrice'"}, "Start price should be greater than 0");
        }
    }

}
