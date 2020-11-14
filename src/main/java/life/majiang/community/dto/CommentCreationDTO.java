package life.majiang.community.dto;

import lombok.Data;

@Data
public class CommentCreationDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
