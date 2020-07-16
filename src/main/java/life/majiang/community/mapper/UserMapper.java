package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into `user`(`account_id`, `name`, `token`, `gmt_create`, `gmt_modified`, `avatar_url`) values(#{accountId}, #{name}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("select *from `user` where token = #{token}")
    @Results(id = "userMap", value = {
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "avatarUrl", column = "avatar_url")
    })
    User findByToken(@Param("token") String token);

    @Select("select * from `user` where id = #{id}")
    @ResultMap("userMap")
    User findById(@Param("id") Integer id);
}
