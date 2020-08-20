package life.majiang.community.interceptor;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 叶志伟 on 2020/7/1.
 */
public class AdService {
    @Autowired
    private AdMapper adMapper;
    public Object list(String name) {
        AdExample navExample=new AdExample();
        navExample.createCriteria().andStatusEqualTo(1)
                .andPosEqualTo(name).andGmtCreateLessThan(System.currentTimeMillis());
        return adMapper.selectByExample(navExample);
    }
}
