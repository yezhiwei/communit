package life.majiang.community.controller;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 叶志伟 on 2020/6/6.
 */
@Controller
public class HelloController {

    @Autowired
    private QuestionService questionService;
    @GetMapping("/hello")
    public  String hello(@RequestParam(name = "name") String name,@RequestParam(name = "yy") String yy, Model model){
        //http://localhost:8887/hello?name=fds&yy=12必须要输入两个参数，如果不加@RequestParam(name = "yy")，可以输入http://localhost:8887/hello?name=fsd
        model.addAttribute("name",name);
        return "hello";
    }
    @GetMapping("/")
    public  String index(HttpServletRequest request,Model model,
                         @RequestParam(name = "page",defaultValue = "1")int page,//不传入page的话，默认是第一页
                         @RequestParam(name = "size",defaultValue = "4")int size){

        PaginationDTO pagination=questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
