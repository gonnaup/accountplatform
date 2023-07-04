package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.RolePermission;
import org.gonnaup.accountplatform.account.entity.RolePermissionPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionPk> {

    List<RolePermission> findByPermissionId(Integer permissionId);

    List<RolePermission> findByRoleId(Integer roleId);


    int countByPermissionId(Integer permissionId);

    int countByRoleId(Integer roleId);

}