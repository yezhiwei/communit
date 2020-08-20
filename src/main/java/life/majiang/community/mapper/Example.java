package life.majiang.community.mapper;

/**
 * Created by 叶志伟 on 2020/7/4.
 */
public class Example {
    public static void main(String[] args) {
        Example example=new Example();
        example.isUnique("abc_______");
    }
    public boolean isUnique(String str) {
        char[] cc;
        cc=str.toCharArray();
        int ss=cc.length;
        System.out.println(ss);
        for (int i=0; i<cc.length;i++ ){
            for (int j=0;i< cc.length;j++ )
                if (cc[i]==cc[j]){
                    return false;
                }
        }
        return true;
    }

}
