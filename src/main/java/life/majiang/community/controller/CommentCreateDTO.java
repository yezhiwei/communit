package life.majiang.community.controller;

import lombok.Data;

/**
 * Created by 叶志伟 on 2020/7/2.
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
