package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.RolePermission;
import org.gonnaup.accountplatform.account.entity.RolePermissionPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionPk> {


    List<RolePermission> findByPermissionId(Integer permissionId);

    List<RolePermission> findByRoleId(Integer roleId);


    int countByPermissionId(Integer permissionId);

    int countByRoleId(Integer roleId);

    /**
     * 经测试，主键为组合键时，当使用默认deleteById删除方法时，如果没有在delete后手动flush，事务提交后也不会执行删除sql，
     * 而主键非组合键时不存在此问题。重写此删除方法，使用jpql后问题得以解决。deleteAllByIdInBatch方法可直接执行删除sql。
     *
     * @param pk must not be {@literal null}.
     */
    @Override
    @Modifying
    @Query("delete from RolePermission p where p.id.roleId = :#{#pk.roleId} and p.id.permissionId = :#{#pk.permissionId}")
    void deleteById(RolePermissionPk pk);
}