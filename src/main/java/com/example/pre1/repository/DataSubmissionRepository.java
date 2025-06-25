package com.example.pre1.repository;

import com.example.pre1.model.DataSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSubmissionRepository extends JpaRepository<DataSubmission, Long> {

    /**
     * 根据用户ID查找所有提交记录。
     * Spring Data JPA 的魔法：我们不需要写任何SQL，它会根据方法名自动为我们生成查询。
     * @param userId 用户的ID
     * @return 该用户的所有提交记录列表
     */
    List<DataSubmission> findByUserId(Long userId);
}
