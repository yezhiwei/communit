package life.majiang.community.interceptor;

import lombok.Data;

/**
 * Created by 叶志伟 on 2020/7/2.
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
