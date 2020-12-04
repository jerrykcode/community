package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long notificationId;
    private User notifier;
    private Integer type;
    private Long outerId;
    private String outerTitle;
    private Long gmtSend;
}
