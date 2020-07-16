package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into `question`(`title`, `description`, `tag`, `gmt_create`, `gmt_modified`, `creator_id`) " +
            "values(#{title}, #{description}, #{tag}, #{gmtCreate}, #{gmtModified}, #{creatorId})")
    public void create(Question question);

    @Select("select * from `question`")
    @Results(id="questionMap", value={
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count")
    })
    List<Question> list();
}
