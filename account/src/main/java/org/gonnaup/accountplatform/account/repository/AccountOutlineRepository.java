package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountOutlineRepository extends JpaRepository<AccountOutline, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update AccountOutline a set a.accountName = ?2, a.updateTime = ?3 where a.id = ?1 ")
    int updateAccountName(Long id, String accountName, LocalDateTime updateTime);

    @Modifying(clearAutomatically = true)
    @Query("update AccountOutline a set a.state = ?2, a.updateTime = ?3 where a.id = ?1")
    int updateAccountState(Long id, String state, LocalDateTime updateTime);

    @Modifying(clearAutomatically = true)
    @Query("update AccountOutline a set a.avatarUrl = ?2, a.updateTime = ?3 where a.id = ?1")
    int updateAvatarUrl(Long id, String avatarUrl, LocalDateTime updateTime);
    
    int countByAccountName(String accountName);

    Optional<AccountOutline> findByAccountName(String accountName);
}