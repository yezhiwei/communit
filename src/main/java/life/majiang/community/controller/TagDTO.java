package life.majiang.community.controller;

import lombok.Data;

import java.util.List;

/**
 * Created by 叶志伟 on 2020/6/30.
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
