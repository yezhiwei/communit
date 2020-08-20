package life.majiang.community.controller;

/**
 * Created by 叶志伟 on 2020/7/2.
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private int type;
    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(int type){
        for (CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()){
            if (commentTypeEnum.getType()==type){
                return true;
            }
        }
        return false;
    }
}
