package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 叶志伟 on 2020/6/27.
 */
public class IndexController1 {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public  String index(HttpServletRequest request, Model model,
                         @RequestParam(name = "page",defaultValue = "1")int page,//不传入page的话，默认是第一页
                         @RequestParam(name = "size",defaultValue = "4")int size,
                         @RequestParam(name = "search",required = false)String search,
                         @RequestParam(name="sort",required = false)String sort,
                         @RequestParam(name="tag",required = false)String tag){

        PaginationDTO pagination=questionService.list1(page,size,search,sort,tag);

        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
