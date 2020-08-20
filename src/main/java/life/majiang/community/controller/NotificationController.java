package life.majiang.community.controller;

import life.majiang.community.interceptor.NotificationDTO;
import life.majiang.community.interceptor.NotificationService;
import life.majiang.community.interceptor.NotificationTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 叶志伟 on 2020/7/7.
 */
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("notification/{id}")
    public String profile(HttpServletRequest request, @PathVariable(name = "id")Long id){
        Object user = request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.
                read(id,user);
        if (NotificationTypeEnum.REPLY_comment.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }
    }
}
