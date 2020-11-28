package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreationDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreationDTO commentCreationDTO,
                        HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreationDTO == null || StringUtils.isBlank(commentCreationDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        else if (commentCreationDTO.getContent().length() > 1024) {
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_TOO_LONG);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreationDTO.getParentId());
        comment.setContent(commentCreationDTO.getContent());
        comment.setType(commentCreationDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value="/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable(name = "id")Long id) {
        List<CommentDTO> commentDTOS = commentService.listByCommentId(id);
        return ResultDTO.okOf(commentDTOS);
    }
}
