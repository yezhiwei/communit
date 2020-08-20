package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.interceptor.NotificationService;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by codedrinker on 2019/5/15.
 */
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    //@Autowired
    //private NotificationService notificationService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;


    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,//这个是{action}参数，这个是用/后面跟着参数，另外的是用？传参的
                          Model model,//model用于发送到页面上去
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size
                           ) {


        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
           PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
          model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }

        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",paginationDTO);

        return "profile";
    }
}
