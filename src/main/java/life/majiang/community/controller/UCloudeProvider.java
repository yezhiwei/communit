package life.majiang.community.controller;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by 叶志伟 on 2020/7/3.
 */
public class UCloudeProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("{ucloud.ufile.privaate-key}")
    private String privatekey;
    public String upload(InputStream inputStream, String contentType, String originalFilename) {
        String generatedFileName;
        String[] filePaths=originalFilename.split("\\.");
        if (filePaths.length>1){
            generatedFileName= UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else{
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try{
            ObjectAuthorization objectAuthorization=new UfileObjectLocalAuthorization(publicKey,privatekey);
            ObjectConfig config=new ObjectConfig(region,suffix);
            UfileClient.object(objectAuthorization,config);
            if (response!=null&&response.getRetCode()==0){
                String url=UfileClient.object(objectAuthorization,config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName,bucketName).createUrl();
            }

        }catch (Exception e){

        }

    }
}
