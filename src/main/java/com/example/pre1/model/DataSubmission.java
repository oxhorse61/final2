package com.example.pre1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_submissions")
public class DataSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private String name;
    private Integer age;
    private String gender;

    // --- 新增字段 ---
    private Double height; // 身高 (单位: cm)
    private Double weight; // 体重 (单位: kg)
    // --- 新增结束 ---

    private String faceImageUrl;
    private String tongueImageUrl;

    @Column(name = "submission_time")
    private LocalDateTime submissionTime;

    // --- Getters 和 Setters ---

    // ... 已有的 Getters 和 Setters 保持不变 ...

    // --- 为新字段添加 Getters 和 Setters ---
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    // --- 新增结束 ---

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getFaceImageUrl() { return faceImageUrl; }
    public void setFaceImageUrl(String faceImageUrl) { this.faceImageUrl = faceImageUrl; }
    public String getTongueImageUrl() { return tongueImageUrl; }
    public void setTongueImageUrl(String tongueImageUrl) { this.tongueImageUrl = tongueImageUrl; }
    public LocalDateTime getSubmissionTime() { return submissionTime; }
    public void setSubmissionTime(LocalDateTime submissionTime) { this.submissionTime = submissionTime; }
}
