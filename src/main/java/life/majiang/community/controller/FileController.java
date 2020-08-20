package life.majiang.community.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 叶志伟 on 2020/7/3.
 */
@Controller
@Slf4j
public class FileController {
    @Autowired
    private UCloudeProvider cloudeProvider;
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
        MultipartFile file= multipartRequest.getFile("editormd-image-file");
        String fileName =cloudeProvider.upload(file.getInputStream(),file.getContentType(),file.getOriginalFilename());
        FileDTO fileDTO=new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(fileName);
        return fileDTO;
    }

}
