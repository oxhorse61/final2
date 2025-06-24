package com.example.pre1.repository; // 请确保这里的包名和您自己的项目一致

import com.example.pre1.model.DataSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 声明这是一个 Spring 管理的 Repository Bean
public interface DataSubmissionRepository extends JpaRepository<DataSubmission, Long> {
    // 暂时不需要添加任何自定义方法
}