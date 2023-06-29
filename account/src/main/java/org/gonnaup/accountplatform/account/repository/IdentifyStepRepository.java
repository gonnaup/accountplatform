package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentifyStepRepository extends JpaRepository<IdentifyStep, Integer> {

    @Override
    boolean existsById(Integer id);

    boolean existsByIdentifyName(String identifyName);

    Optional<IdentifyStep> findByIdentifyName(String identifyName);

    /**
     * 更新数据到下一段
     * <a href="https://codingexplained.com/coding/java/spring-framework/updating-entities-with-update-query-spring-data-jpa">巨坑，各种测试耗时超半天</a>
     *
     * @param stepId
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update IdentifyStep t set t.identifyBegin = t.identifyBegin + t.identifyInterval where t.id = ?1")
    int toNextIdentifyStep(Integer stepId);

}