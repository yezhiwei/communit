package life.majiang.community.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by 叶志伟 on 2020/7/8.
 */
public class HotTagCache {
    private List<String> hots=new ArrayList<>();
    public void updateTags(Map<String, Integer> tags) {
        int max=10;
        PriorityQueue<HotTagDto> pri=new PriorityQueue<>();
        tags.forEach((name,priority)->{
            HotTagDto h=new HotTagDto();
            h.setName(name);
            h.setPriority(priority);
            if (pri.size()<max){
                pri.add(h)
            }else{
                HotTagDto minHot=pri.peek();
                if (h.compareTo(minHot)>0){
                    pri.poll();
                    pri.add(h);
                }
            }
        });
        List<String> sortedTags=new ArrayList<>();
        HotTagDto poll=pri.poll();
        while (poll!=null){
            sortedTags.add(poll.getName());
            poll=pri.poll();
        }
        hots=sortedTags;
    }
}
