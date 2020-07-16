package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model) {
        User user = currentUser.getCurrentUser(request);
        if (user != null)
            request.getSession().setAttribute("user", user);

        List<QuestionDTO> questionDTOList = questionService.list();
        model.addAttribute("questions", questionDTOList);
        return "index";
    }
}
