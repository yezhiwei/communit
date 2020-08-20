package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

/**
 * Created by 叶志伟 on 2020/6/11.
 */
@Data
public class CommentDTO {
    private Long parentId;
    private String content;
    private Integer type;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
}
