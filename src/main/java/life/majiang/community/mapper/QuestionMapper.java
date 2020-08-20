package life.majiang.community.mapper;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.model.Question;
import life.majiang.community.service.QuestionExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by 叶志伟 on 2020/6/7.
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,comment_count,view_count,like_count,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") int offset,@Param(value = "size") int size);//当参数超过两个的时候要加@Param

    @Select("select count(1) from QUESTION")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId")Integer userId,@Param(value = "offset")  int offset,@Param(value = "size") Integer size);

    @Select("select count(1) from QUESTION where creator=#{userId}")
    int countByUserId(int userId);

    @Select("select * from question where id=#{id} ")
    Question getById(int id);



    int updateByExampleSelective(@Param("record") Question record, @Param("example") QuestionExample example);

    void insert(Question question);

    Question selectByPrimarykey(Integer id);

    long countByExample(QuestionExample questionExample);

    List<Question> selectByExampleWithRowbounds(QuestionExample example, RowBounds rowBounds);

    life.majiang.community.controller.Question selectByPrimaryKey(Long parentId);
}
