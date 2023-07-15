package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Override
    <S extends Permission> S save(S entity);

    @Override
    void deleteById(Integer integer);

    /**
     * 查询下一个权限位
     *
     * @return
     */
    @Query("select MAX(p.permissionLocation) + 1 from Permission p")
    Integer findNextPermissionLocation();


}