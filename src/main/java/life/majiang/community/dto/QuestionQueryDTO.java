package life.majiang.community.dto;

import lombok.Data;

/**
 * Created by 叶志伟 on 2020/6/30.
 */
@Data

public class QuestionQueryDTO {
    private String search;
    private String sort;
    private Long time;
    private String tag;
    private Integer page;
    private Integer size;
}
