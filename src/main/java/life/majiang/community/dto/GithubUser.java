package life.majiang.community.dto;

import lombok.Data;

/**
 * Created by 叶志伟 on 2020/6/6.
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

}
