package com.example.pre1.controller;

import com.example.pre1.model.DataSubmission;
import com.example.pre1.repository.DataSubmissionRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/data-submissions")
public class DataSubmissionController {

    @Autowired
    private DataSubmissionRepository dataSubmissionRepository;

    @Autowired
    private MinioClient minioClient;

    @GetMapping
    public List<DataSubmission> getAllSubmissions() {
        return dataSubmissionRepository.findAll();
    }

    @PostMapping
    public String submitData(@RequestParam("name") String name,
                             @RequestParam("age") Integer age,
                             @RequestParam("gender") String gender,
                             @RequestParam("faceImage") MultipartFile faceImage,
                             @RequestParam("tongueImage") MultipartFile tongueImage) {

        System.out.println("==================== NEW SUBMISSION RECEIVED ====================");
        System.out.println(">>> 开始处理提交，接收到的姓名: " + name);
        System.out.println(">>> 接收到的面部图片文件名: " + faceImage.getOriginalFilename() + ", 大小: " + faceImage.getSize());
        System.out.println(">>> 接收到的舌象图片文件名: " + tongueImage.getOriginalFilename() + ", 大小: " + tongueImage.getSize());

        try {
            System.out.println(">>> 准备上传面相图片到 MinIO...");
            String faceImageUrl = uploadFileToMinio(faceImage);
            System.out.println(">>> 面相图片上传成功，URL: " + faceImageUrl);

            System.out.println(">>> 准备上传舌象图片到 MinIO...");
            String tongueImageUrl = uploadFileToMinio(tongueImage);
            System.out.println(">>> 舌象图片上传成功，URL: " + tongueImageUrl);

            DataSubmission submission = new DataSubmission();
            submission.setName(name);
            submission.setAge(age);
            submission.setGender(gender);
            submission.setFaceImageUrl(faceImageUrl);
            submission.setTongueImageUrl(tongueImageUrl);
            submission.setSubmissionTime(LocalDateTime.now());

            System.out.println(">>> 准备将数据保存到数据库...");
            DataSubmission savedSubmission = dataSubmissionRepository.save(submission);
            System.out.println(">>> 数据成功保存到数据库！生成的 ID: " + savedSubmission.getId());

            System.out.println("==================== SUBMISSION PROCESSED SUCCESSFULLY ====================");
            return "数据保存成功";

        } catch (Exception e) {
            System.err.println("!!! 数据保存过程中发生严重错误 !!!");
            e.printStackTrace();
            return "数据保存失败: " + e.getMessage();
        }
    }

    private String uploadFileToMinio(MultipartFile file) throws Exception {
        // 防止文件名为空
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("上传的文件名不能为空");
        }

        // 生成一个独一无二的文件名，防止重名覆盖
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        System.out.println("    [MinIO] 正在上传文件，生成的唯一文件名: " + fileName);

        // 执行上传操作
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("images")
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // 返回可供访问的完整 URL (使用局域网IP)
        String url = "http://192.168.0.102:9000/images/" + fileName; // 请确保这个 IP 和您 minio 启动时显示的 IP 一致
        System.out.println("    [MinIO] 文件上传完成，访问地址: " + url);
        return url;
    }
}