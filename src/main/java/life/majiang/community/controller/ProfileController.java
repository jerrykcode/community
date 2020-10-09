package life.majiang.community.controller;

import life.majiang.community.dto.PageDTO;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action,
                          @RequestParam(name="page", defaultValue = "1") Integer pageNo,
                          @RequestParam(name="size", defaultValue = "5") Integer pageListNum,
                          HttpServletRequest request,
                          Model model) {
        if (action.equals("questions")) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        }
        else if (action.equals("replies")) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        User user = currentUser.getCurrentUser(request);
        if (user != null)
            request.getSession().setAttribute("user", user);
        else return "redirect:/";

        if (action.equals("questions")) {
            PageDTO pageDTO = questionService.listByCreatorId(pageNo, pageListNum, user);
            model.addAttribute("page", pageDTO);
        }
        return "profile";
    }
}
