package life.majiang.community.service;

import life.majiang.community.dto.PageDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO<QuestionDTO> list(Integer pageNo, Integer pageListsNum) {

        Integer offset = pageListsNum * (pageNo - 1);

        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample,
                new RowBounds(offset, pageListsNum));
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        pageDTO.setTList(questionDTOList);
        pageDTO.setPagination(totalCount, pageNo, pageListsNum);
        return pageDTO;
    }

    public PageDTO<QuestionDTO> listByCreatorId(Integer pageNo, Integer pageListNum, User creator) {

        Integer offset = pageListNum * (pageNo - 1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorIdEqualTo(creator.getId());
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, pageListNum));
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();
        for (Question question : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(creator);
            questionDTOList.add(questionDTO);
        }

        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        pageDTO.setTList(questionDTOList);

        pageDTO.setPagination((int)questionMapper.countByExample(questionExample), pageNo, pageListNum);
        return pageDTO;
    }

    public QuestionDTO getByQuestionId(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(userMapper.selectByPrimaryKey(question.getCreatorId()));
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) { //创建
            questionMapper.insert(question);
        }
        else { //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectedRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String tags[] = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questionList = questionExtMapper.selectRelated(question);
        return questionList.stream().map(q->{
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
    }
}
