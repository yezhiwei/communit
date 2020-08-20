package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 叶志伟 on 2020/6/11.
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request){
        User user =(User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment=new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentator(user.getId().longValue());
        commentService.insert(comment,user);
        return ResultDTO.okof();
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOs=commentService.listByTargetId(id,CommentTypeEnum.COMMENT);
        return ResultDTO.okof(commentDTOs);
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments1(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOs=commentService.listByTargetId(id,CommentTypeEnum.COMMENT);
        return ResultDTO.okof(commentDTOs);
    }
}
