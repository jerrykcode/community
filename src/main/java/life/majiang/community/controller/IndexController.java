package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private CurrentUser currentUser;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        User user = currentUser.getCurrentUser(request);
        if (user != null)
            request.getSession().setAttribute("user", user);
        return "index";
    }
}
