package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into `question`(`title`, `description`, `tag`, `gmt_create`, `gmt_modified`, `creator_id`) " +
            "values(#{title}, #{description}, #{tag}, #{gmtCreate}, #{gmtModified}, #{creatorId})")
    public void create(Question question);

    @Select("select * from `question` limit #{offset}, #{size}")
    @Results(id="questionMap", value={
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "creatorId", column = "creator_id"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "viewCount", column = "view_count"),
            @Result(property = "likeCount", column = "like_count")
    })
    List<Question> findAll(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select * from `question` where `creator_id` = #{creator_id} limit #{offset}, #{size}")
    @ResultMap(value = {"questionMap"})
    List<Question> findByCreatorId(@Param("offset") Integer offset, @Param("size") Integer size, @Param("creator_id") Integer creatorId);

    @Select("select count(1) from `question`")
    Integer count();

    @Select("select count(1) from `question` where `id` = #{id}")
    Integer countWithCreatorId(@Param("id") Integer id);
}
