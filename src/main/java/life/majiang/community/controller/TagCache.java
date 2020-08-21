package life.majiang.community.controller;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 叶志伟 on 2020/6/30.
 */
public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> list=new ArrayList<>();
        TagDTO tagDTO=new TagDTO();
        tagDTO.setTags(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "golang", "objective-c", "typescript", "shell", "swift", "c#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTO.setCategoryName("开发语言");
        list.add(tagDTO);
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        list.add(framework);
        return list;
    }

    public static String filterInvalid(String tags) {
        String[] split= StringUtils.split(tags,",");
        List<TagDTO> tagDtos=get();
        //判断所有的split都包含再tagdtos种
        List<String> taglist=tagDtos.stream().flatMap(tag->tag.getTags().stream()).collect(Collectors.toList());
        String invalud = Arrays.stream(split).filter(t-> org.apache.commons.lang3.StringUtils.isBlank(t) || !taglist.contains(t)).collect(Collectors.joining(","));
        return invalud;

    }

    public static  String filterInvalud1(String tags){
        String[] split=StringUtils.split(tags,",");
        List<TagDTO> tagDTOs=get();

        return null;
    }


}
