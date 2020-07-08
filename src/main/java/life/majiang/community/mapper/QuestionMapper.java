package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {

    @Insert("insert into `question`(`title`, `description`, `tag`, `gmt_create`, `gmt_modified`, `creator_id`) " +
            "values(#{title}, #{description}, #{tag}, #{gmtCreate}, #{gmtModified}, #{creatorId})")
    public void create(Question question);
}
