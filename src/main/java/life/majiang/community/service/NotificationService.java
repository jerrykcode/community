package life.majiang.community.service;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PageDTO;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.mapper.CommentMapper;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    public PageDTO<NotificationDTO> list(Integer pageNo, Integer pageListsNum, User user) {

        Integer offset = (pageNo - 1) * pageListsNum;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(user.getId());
        Integer totalNotificationCount = (int) notificationMapper.countByExample(notificationExample);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper
                .selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, pageListsNum));
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationId(notification.getId());
            notificationDTO.setType(notification.getType());
            notificationDTO.setGmtSend(notification.getGmtCreate());

            //通过通知者的id查找通知者
            Long notifier = notification.getNotifier();
            notificationDTO.setNotifier(userMapper.selectByPrimaryKey(notifier));

            //设置outerId
            Long outerId = notification.getOuterId();
            notificationDTO.setOuterId(outerId);
            //通过outerId查找outerTitle
            String outerTitle = "";
            if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.ordinal()
                    || notification.getType() == NotificationTypeEnum.LIKE_QUESTION.ordinal())
            { //若对Question进行评论或点赞
                QuestionExample questionExample = new QuestionExample();
                questionExample.createCriteria().andIdEqualTo(outerId);
                List<Question> questions = questionMapper.selectByExample(questionExample);
                if (questions.size() > 0)
                    outerTitle = questions.get(0).getTitle();
            }
            else if (notification.getType() == NotificationTypeEnum.REPLY_COMMENT.ordinal()
                    || notification.getNotifier() == NotificationTypeEnum.LIKE_COMMENT.ordinal())
            { //若对Comment进行评论或点赞
                CommentExample commentExample = new CommentExample();
                commentExample.createCriteria().andIdEqualTo(outerId);
                List<Comment> comments = commentMapper.selectByExample(commentExample);
                if (comments.size() > 0)
                    outerTitle = comments.get(0).getContent();
            }
            notificationDTO.setOuterTitle(outerTitle);

            notificationDTOS.add(notificationDTO);
        }

        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        pageDTO.setTList(notificationDTOS);
        pageDTO.setPagination(totalNotificationCount, pageNo, pageListsNum);

        return pageDTO;
    }
}
