package life.majiang.community.controller;

import life.majiang.community.cache.TagCache;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    private static int count = 0;
    @GetMapping("/publish")
    public String publish(HttpServletRequest request,
                            Model model) {
        User user = (User) request.getSession().getAttribute("user") ;
        if (user != null)
            request.getSession().setAttribute("user", user);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String postPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
                    HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        String errorMsg = null;
        if (title == null || title.equals("")) {
            errorMsg = "标题不能为空";
        }
        else if (title.length() > 50) {
            errorMsg = "标题不能超过50字符";
        }
        else if (description == null || description.equals("")) {
            errorMsg = "问题补充不能为空";
        }
        else if (tag == null || tag.equals("")) {
            errorMsg = "标签不能为空";
        }
        else {
            String invalid = TagCache.filterValid(tag);
            if (!StringUtils.isEmpty(invalid)) {
                errorMsg = "输入非法标签: " + invalid;
            }
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
        question.setId(id);
        if (id == null) {
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
        }

        questionService.createOrUpdate(question);
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,
                       Model model) {
        QuestionDTO questionDTO = questionService.getByQuestionId(id);
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tag", questionDTO.getTag());
        model.addAttribute("id", id);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
}
