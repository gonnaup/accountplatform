package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Modifying(clearAutomatically = true)
    @Query("update Role r set r.permissionCode = ?2, r.updateTime = ?3 where r.id = ?1")
    int updatePermissionCodeOfRole(Integer roleId, String permissionCode, LocalDateTime updateTime);

}