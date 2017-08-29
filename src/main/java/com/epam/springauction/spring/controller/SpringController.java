package com.epam.springauction.spring.controller;

//import javax.validation.Valid;
import com.epam.springauction.daoInterfaces.CustomerDAO;
import com.epam.springauction.daoInterfaces.ItemDAO;
import com.epam.springauction.entities.Item;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Mikhail_Bobriashov
 */
@Controller
public class SpringController {

    @Autowired // ищет @Component
    private ItemDAO itemDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private Validator validator;

    @RequestMapping(value = {"/advancesearch"}, method = RequestMethod.GET)
    public String processItemFinding(ModelMap model) {
        Item item = new Item();
        model.addAttribute("search-item", item);
        //return new ModelAndView("advancesearch", "search-item", new Item());
        return "advancesearch";

    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String findItem(@ModelAttribute("search-item") Item item, BindingResult br, ModelMap model) {

        // item validation
        validator.validate(item, br);

        if (br.hasErrors()) {
            return "advancesearch";
        }

        List list = itemDAO.advanceFind(item);
        model.addAttribute("allitems", list);

        return "showitems";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Boolean isInTable = customerDAO.springCheckCustomer(login, password);

        if (isInTable) {
            HttpSession session = request.getSession();
            // если сессия не новая, то уничтожаем ее и создаем новую
            if (!session.isNew()) {
                session.invalidate();
                session = request.getSession(true);
                session.setAttribute("login", login);
            } else {
                session.setAttribute("login", login);
            }

            return "redirect:/serv/showitems";
        } else {
            return "/errorpages/loginerror";
        }
    }

}
