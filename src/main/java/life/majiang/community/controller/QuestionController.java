package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by 叶志伟 on 2020/6/9.
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") String id, Model model){//model用于展示到页面上去
        Long questionId=null;
        questionId=Long.parseLong(id);
        int q=questionId.intValue();
        QuestionDTO questionDTO=questionService.getById(q);
        List<QuestionDTO> relatedQuestions=questionService.selectRelated(questionDTO);
        List<CommentDTO> comments=commentService.listByTargetId(questionId,CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(questionId.intValue());
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";


 /*       QuestionDTO questionDTO =questionService.getById(id);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";*/
    }
}
