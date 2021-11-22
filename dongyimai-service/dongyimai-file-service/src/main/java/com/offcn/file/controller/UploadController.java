package com.offcn.file.controller;

import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.file.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    String FILE_SERVER_URL;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");

            String originalFilename = file.getOriginalFilename();// 1.jpg
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            String s = fastDFSClient.uploadFile(file.getBytes(), extName);

            String url = FILE_SERVER_URL + s;//图片完整的url地址

            return new Result(true, StatusCode.OK, "上传成功", url);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败");
        }
    }

}
