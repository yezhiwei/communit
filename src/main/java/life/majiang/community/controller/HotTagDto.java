package life.majiang.community.controller;

import lombok.Data;

/**
 * Created by 叶志伟 on 2020/7/8.
 */
@Data
public class HotTagDto implements Comparable{
    private String name;
    private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority()-((HotTagDto) o).getPriority();
    }
}
