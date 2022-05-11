package com.simo.reggie.controller;

import com.simo.reggie.commons.R;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    String basePath;

    /**
     * 文件上传
     * @param request
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public R<String> upload(HttpServletRequest request,MultipartFile file){
        System.out.println(file);
        File imgDir = new File(basePath);
        if(!imgDir.exists()){
            imgDir.mkdirs();
        }
        //使用UUID重命名文件，防止因为文件名一样而覆盖
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString().concat(suffix);

        try {
            //一定要使用绝对路径
            file.transferTo(new File(imgDir.getAbsolutePath()+"/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @RequestMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(basePath + name));
            outputStream = response.getOutputStream();
            int copy = IOUtils.copy(fileInputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

