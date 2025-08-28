package com.situ.kdz2026.service.impl;

import com.situ.kdz2026.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class UploadServiceImpl implements UploadService {
    @Value("${upload.location}")
    private String uploadLocation;//文件上传路径

    /**
     *
     * @param file 上传文件实体
     * @param type 上传文件类型
     * @return
     */
    @Override
    public String uploadImage(MultipartFile file, String type) {
        //1.创建目录
        File dir = new File(uploadLocation + "/images/" + type);
        if (!dir.exists()) {
            boolean b = dir.mkdirs();//级联创建目录
            if (!b) {
                throw new RuntimeException("级联创建目录异常");
            }
        }
        //2.给上传的文件起名
        LocalDateTime now = LocalDateTime.now();
        String fileName = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Random random = new Random();
        int sid = random.nextInt(1000);//0~999
        fileName = fileName + "-" + sid;

        //3.拼扩展名
        String originalFileName = file.getOriginalFilename();//上传文件名
        //assert originalFileName != null;
        int idx = originalFileName.lastIndexOf(".");
        String ext = originalFileName.substring(idx);
        fileName = fileName + ext;

        //完整的文件名
        String fullName = dir.getAbsolutePath() + "/" + fileName;

        //要存储的目标文件
        File target = new File(fullName);

        //4.存储文件
        try {
            file.transferTo(target);
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败");
        }

        //5.返回访问地址和存储地址
        return "/images/" + type + "/" + fileName;
    }
}
