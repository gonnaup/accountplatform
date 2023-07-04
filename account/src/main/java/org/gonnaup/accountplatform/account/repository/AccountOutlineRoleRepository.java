package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.AccountOutlineRole;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOutlineRoleRepository extends JpaRepository<AccountOutlineRole, AccountOutlineRolePk> {


    /**
     * 查询某帐号的所有关联权限列表
     *
     * @param accountOutlineId 帐号Id
     * @return 帐号的关联角色列表
     */
    List<AccountOutlineRole> findByAccountOutlineId(Long accountOutlineId);

    /**
     * 查询某角色关联的帐号列表
     *
     * @param roleId 角色Id
     * @return 角色关联的帐号列表
     */
    List<AccountOutlineRole> findByRoleId(Integer roleId);


    /**
     * 统计某帐号关联的角色个数
     *
     * @param accountOutlineId 帐号Id
     * @return 帐号关联角色个数
     */
    int countByAccountOutlineId(Long accountOutlineId);

    /**
     * 统计某角色关联的帐号个数
     *
     * @param roleId
     * @return
     */
    long countByRoleId(Integer roleId);

}