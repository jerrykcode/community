package life.majiang.community.controller;

import life.majiang.community.dto.PageDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
                        @RequestParam(name = "size", defaultValue = "5") Integer pageListNum) {
        PageDTO<QuestionDTO> pageDTO = questionService.list(pageNo, pageListNum);
        model.addAttribute("page", pageDTO);
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "index";
    }

}
