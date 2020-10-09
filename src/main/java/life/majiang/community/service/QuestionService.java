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

        List<Question> questionList = questionMapper.findAll(offset, pageListsNum);
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

    public PageDTO listByCreatorId(Integer pageNo, Integer pageListNum, User creator) {

        Integer offset = pageListNum * (pageNo - 1);

        List<Question> questionList = questionMapper.findByCreatorId(offset, pageListNum, creator.getId());
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(creator);
            questionDTOList.add(questionDTO);
        }

        PageDTO pageDTO = new PageDTO();
        pageDTO.setQuestions(questionDTOList);
        pageDTO.setPagination(questionMapper.countWithCreatorId(creator.getId()), pageNo, pageListNum);
        return pageDTO;
    }
}
