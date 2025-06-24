package com.example.pre1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_submissions")
public class DataSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer age;
    private String gender;

    private String faceImageUrl;
    private String tongueImageUrl;

    @Column(name = "submission_time")
    private LocalDateTime submissionTime;

    // --- Getters 和 Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFaceImageUrl() {
        return faceImageUrl;
    }

    public void setFaceImageUrl(String faceImageUrl) {
        this.faceImageUrl = faceImageUrl;
    }

    public String getTongueImageUrl() {
        return tongueImageUrl;
    }

    public void setTongueImageUrl(String tongueImageUrl) {
        this.tongueImageUrl = tongueImageUrl;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }
}