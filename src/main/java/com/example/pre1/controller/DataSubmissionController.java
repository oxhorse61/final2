package com.example.pre1.controller;

import com.example.pre1.model.DataSubmission;
import com.example.pre1.model.User;
import com.example.pre1.repository.DataSubmissionRepository;
import com.example.pre1.repository.UserRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/data-submissions")
@Validated // 开启对方法参数的验证
public class DataSubmissionController {

    @Autowired
    private DataSubmissionRepository dataSubmissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MinioClient minioClient;

    @GetMapping
    public List<DataSubmission> getMySubmissions(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("认证失败，未找到用户: " + username));
        return dataSubmissionRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<String> submitData(Authentication authentication,
                                             @RequestParam("name") String name,
                                             // --- 新增后端验证 ---
                                             @RequestParam("age") @Min(1) @Max(120) Integer age,
                                             @RequestParam("gender") String gender,
                                             @RequestParam("height") @Min(50) @Max(250) Double height,
                                             @RequestParam("weight") @Min(10) @Max(300) Double weight,
                                             // --- 验证结束 ---
                                             @RequestParam("faceImage") MultipartFile faceImage,
                                             @RequestParam("tongueImage") MultipartFile tongueImage) {
        try {
            User currentUser = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("错误: 找不到当前登录用户."));

            String faceImageUrl = uploadFileToMinio(faceImage);
            String tongueImageUrl = uploadFileToMinio(tongueImage);

            DataSubmission submission = new DataSubmission();
            submission.setUser(currentUser);
            submission.setName(name);
            submission.setAge(age);
            submission.setGender(gender);
            // --- 保存新数据 ---
            submission.setHeight(height);
            submission.setWeight(weight);
            // --- 保存结束 ---
            submission.setFaceImageUrl(faceImageUrl);
            submission.setTongueImageUrl(tongueImageUrl);
            submission.setSubmissionTime(LocalDateTime.now());

            dataSubmissionRepository.save(submission);

            return ResponseEntity.ok("数据保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("数据保存失败: " + e.getMessage());
        }
    }

    private String uploadFileToMinio(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空或文件名为空");
        }
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("images")
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        // 请确保这个URL和您的MinIO配置一致
        return "http://127.0.0.1:9000/images/" + fileName;
    }
}
