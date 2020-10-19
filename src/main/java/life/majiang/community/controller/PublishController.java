package life.majiang.community.controller;

import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    private static int count = 0;
    @GetMapping("/publish")
    public String publish(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user") ;
        if (user != null)
            request.getSession().setAttribute("user", user);
        return "publish";
    }

    @PostMapping("/publish")
    public String postPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        String errorMsg = null;
        if (title == null || title.equals("")) {
            errorMsg = "标题不能为空";
        }
        else if (description == null || description.equals("")) {
            errorMsg = "问题补充不能为空";
        }
        else if (tag == null || tag.equals("")) {
            errorMsg = "标签不能为空";
        }
        if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.getSession().setAttribute("user", user);
        }
        else {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreatorId(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        questionMapper.create(question);
        return "publish";
    }
}
