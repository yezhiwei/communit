package life.majiang.community.mapper;

import life.majiang.community.interceptor.UserExample;
import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by 叶志伟 on 2020/6/6.
 */
@Mapper
public interface UserMapper {
    @Insert("insert into user (account_id, name ,token,gmt_Create,gmt_Modified,AVATAR_URL) values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
     void insert(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(String token);

    @Select("select * from user where id=#{id}")
    User findById(int id);

    @Select("select * from user where account_Id=#{accountId}")
    User findByAccountId(String accountId);

    @Update("update user set name=#{name},gmt_modified=#{gmtModified} WHERE id=#{id}")
    void update(User user);
    User selectByPrimaryKey(int id);

    List<User> selectByExample(UserExample userExample);
}
