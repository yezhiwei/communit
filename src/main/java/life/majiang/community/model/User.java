package life.majiang.community.model;

import lombok.Data;

/**
 * Created by 叶志伟 on 2020/6/6.
 */
@Data//自动生成get，set，tostring方法
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;


}
