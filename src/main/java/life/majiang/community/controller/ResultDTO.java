package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import lombok.Data;

import java.util.List;

/**
 * Created by 叶志伟 on 2020/7/2.
 */
@Data
public class ResultDTO<T> {
    private int code;
    private String message;
    private T data;

    public static Object okof() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("成功");
        return resultDTO;
    }

    public static Object errorOf(CustomizeErrorCode noLogin) {
        return errorOf(noLogin.getCode(), noLogin.getMessage());
    }

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO r=new ResultDTO();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }
    public static ResultDTO<List<CommentDTO>> okof(List<CommentDTO> commentDTOs){
        ResultDTO resultDTO=new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("成功");
        resultDTO.setData(commentDTOs);
        return resultDTO;
    }
}