package life.majiang.community.interceptor;

/**
 * Created by 叶志伟 on 2020/7/2.
 */
public enum  NotificationTypeEnum {
    REPLY_question(1,"回复了问题"),
    REPLY_comment(2,"replycomment");
    private int type;
    private String name;
    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }
    public static String nameOfType1(Object type){
        int type1=(int)type;
        for (NotificationTypeEnum no:NotificationTypeEnum.values()){
            if (no.getType()==type1){
                return no.getName();
            }
        }
        return "";
    }

    public static String nameOfType(Object type) {
        int type1 = (int) type;
        for (NotificationTypeEnum no:NotificationTypeEnum.values()){
            if (no.getType()==type1){
                return no.getName();
            }
        }
        return "";
    }
}
