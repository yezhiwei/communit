package life.majiang.community.controller;

//import life.majiang.community.cache.TagCache;
//import life.majiang.community.dto.QuestionDTO;
//import life.majiang.community.model.Question;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
//import life.majiang.community.service.QuestionService;
import life.majiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by codedrinker on 2019/5/2.
 */
@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    QuestionService questionService;

  /*  @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }*/

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") int id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        //Question question = questionMapper.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        //model.addAttribute("tags", TagCache.get());
        return "publish";
    }


    @GetMapping("/publish")
    public String publish(Model model) {
        //model.addAttribute("tags", TagCache.get());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            //@RequestParam(value = "id",required = false) int id,
                            HttpServletRequest request,Model model){
        model.addAttribute("title", title);//model里面的可以直接在页面上得到，在html页面上使用${tag}
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        String invalud=TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalud)){
            model.addAttribute("error","输入非法标签"+invalud);
            return "publish";
        }

      /*  User user=null;
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length!=0)
            for (Cookie c:cookies) {
                if (c.getName().equals("token")){
                    String token=c.getValue();
                     user=userMapper.findByToken(token);
                    if (user!=null){
                        //写到session里面去
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }*/
        User user = (User) request.getSession().getAttribute("user");//使用了拦截器技术，可以通过这种方式来获取user，上面的和前面的页面代码重复了

        if (user==null){
            model.addAttribute("erroryy","用户为登陆");//model里面的可以直接在页面上得到，在html页面上使用${tag}
            return "publish";
        }
        Question question=new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
       // question.setId(id);
        //questionMapper.create(question);
        questionService.createOrUpdate(question);
        return "redirect:/";//没有异常，返回首页

    }

   /* @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }*/
}
