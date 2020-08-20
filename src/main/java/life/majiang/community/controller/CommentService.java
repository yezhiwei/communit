package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.interceptor.Notification;
import life.majiang.community.interceptor.NotificationMapper;
import life.majiang.community.interceptor.NotificationTypeEnum;
import life.majiang.community.interceptor.UserExample;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static life.majiang.community.controller.CommentTypeEnum.COMMENT;
import static life.majiang.community.controller.CommentTypeEnum.commentTypeEnum;

/**
 * Created by 叶志伟 on 2020/7/2.
 */
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;


    @Autowired
    private QuestionExtMapper questionExtMapper;
    public List<CommentDTO> listByTargetId1(Long id,CommentTypeEnum typeEnum)
    {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id)
                .andTypeEqualTo(typeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators=comments.stream().map(comment -> comment.getCommentator()
        .collect(Collectors.toSet()));
        List<Long> userIds=new ArrayList<>();
        userIds.add(commentators);
        //获取评论人并且转换为map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users=userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为CommentDTO
        List<CommentDTO> commetnDTOS=comments.stream().map(
                comment -> {
                    CommentDTO c=new CommentDTO();
                    BeanUtils.copyProperties(comment,c);
                    c.setUser(userMap.get(comment.getCommentator()));
                    return c;
                }
        ).collect(Collectors.toList());
        return commetnDTOS;

    }

    @Transactional//事物
    public void insert1(Comment comment, User commentator) {
        if (comment.getParentId()==null){
            throw  new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbCom=commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbCom==null){
                throw  new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment=new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

        }
    }
    public List<CommentDTO> listByTargetId2(Long questionId, CommentTypeEnum type) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(questionId)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators=comments.stream().map(
                comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转换为map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //转换comment为commentDTO
        List<CommentDTO> commentDTOs=comments.stream().map(
                comment -> {
                    CommentDTO commentDTO=new CommentDTO();
                    BeanUtils.copyProperties(comment,commentDTO);
                    commentDTO.setUser(users.get(comment.getCommentator()))
                    return commentDTO;
                }
        ).collect(Collectors.toList());
        return commentDTOs;

    }
    public List<CommentDTO> listByTargetId(Long questionId,CommentTypeEnum typeEnum){
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(questionId).
                andTypeEqualTo(typeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重评论人
        Set<Long> commentators =  comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转为map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long,User> userMap=users.stream().collect
                (Collectors.toMap(user->user.getId().user->user));
        //吧comment转成commetnDTO
        List<CommentDTO> commentDTOs=comments.stream().map(comment
        ->{
            CommentDTO commentDTO=new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOs;
    }

        //对于问题的评论
    public List<CommentDTO> listByTargetId(Long questionId, CommentTypeEnum type) {
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(questionId)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        //你想要得到List<Comment>，就必须需要在commentMapper调用返回值为这个List<Comment>的方法（selectByExample），
        // 然后又发现selectByExample方法种的参数为CommentExample;所以在前面定义CommentExample commentExample=new CommentExample();
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0){
            return new ArrayList<>();
        }
       //获取去重的评论人
        Set<Long> commentators=comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转成Map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users=userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
    @Transactional
    public void insert22(Comment comment, User commentator) {
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){//回复评论

            commentMapper.insert(comment);

            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            parentComment.setCommentCount(1);
            parentComment.setId(comment.getParentId());
            commentExtMapper.incCommentCount(parentComment);

            Comment dbCommment=new Comment();
            Question question = questionMapper.selectByPrimaryKey(dbCommment.getParentId());//父评论的父评论是问题
            if (question==null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            createNotify(comment,dbCommment.getCommentator(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_comment,question.getId());
        }
        else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
    public List<CommentDTO> listByTargetId(Long questionId,CommentTypeEnum type){
        //这里的type要么是1评论问题Question（1），要么是2评论评论（）2
        CommentExample commentExample=new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(questionId)
                .andTypeEqualTo(type.getType());
        List<Comment> comments=commentMapper.selectByExample(commentExample);
        //获取去重的评论人
        Set<Long> commentators=comments.stream().map(
                comment -> comment.getCommentator()
        ).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转成map


    }

    @Transactional
    public void insert(Comment comment, User user) {
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbcomment==null){
                throw  new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComemtn=new Comment();
            parentComemtn.setId(comment.getParentId());
            parentComemtn.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComemtn);

            //回复问题
            Question question=questionMapper.selectByPrimarykey((int)dbcomment.getParentId());
            if (question==null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //创建通知
            createNotify(comment,dbcomment.getCommentator(),user.getName(),question.getTitle(), NotificationTypeEnum.REPLY_question,question.getId());


        }
    }
    public List<CommentDTO> listByQuestionId(Long id){
        CommentExample c =new CommentExample();
        c.createCriteria().andParentIdEqualTo(id).
                andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample((CommentExample) commentMapper);
        if (comments.size()==0){
            return new ArrayList<>();
        }
        Set<Long> commentators=comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //获取去重的评论人
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并且转成map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        //得到所有users
        List<User> users=userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<CommentDTO> commentDTOs=comments.stream().map(
             comment -> {
                 CommentDTO commentDTO=new CommentDTO();
                 BeanUtils.copyProperties(comment,commentDTO);
                 commentDTO.setUser(userMap.get(comment.getCommentator()));
                 return commentDTO;
             }
        ).collect(Collectors.toList());
        return commentDTOs;
    }

    private void createNotify(Comment comment, Long commentator, String name, String title, NotificationTypeEnum notificationType, Long outerId) {
        Notification notification=new Notification();
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setNotifier(comment.getCommentator());
        notification.setType(notificationType.getType());
        notificationMapper.insert(notification);
    }
}
