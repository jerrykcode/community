package life.majiang.community.service;

import life.majiang.community.dto.PageDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer pageNo, Integer pageListsNum) {

        Integer offset = pageListsNum * (pageNo - 1);

        List<Question> questionList = questionMapper.list(offset, pageListsNum);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        PageDTO pageDTO = new PageDTO();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        Integer totalCount = questionMapper.count();
        pageDTO.setQuestions(questionDTOList);
        pageDTO.setPagination(totalCount, pageNo, pageListsNum);
        return pageDTO;
    }
}
