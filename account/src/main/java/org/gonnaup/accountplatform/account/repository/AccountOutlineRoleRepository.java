package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.AccountOutlineRole;
import org.gonnaup.accountplatform.account.entity.AccountOutlineRolePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOutlineRoleRepository extends JpaRepository<AccountOutlineRole, AccountOutlineRolePk> {


    /**
     * 查询某帐号的所有关联角色ID列表
     *
     * @param accountOutlineId 帐号Id
     * @return 帐号的关联角色列表
     */
    @Query("select t.id.roleId from AccountOutlineRole t where t.id.accountOutlineId = ?1")
    List<Integer> findRoleIdListByAccountOutlineId(Long accountOutlineId);


    /**
     * 查询某帐号未拥有的角色Id列表
     *
     * @param accountOutlineId
     * @return
     */
    @Query("select distinct t.id.roleId from AccountOutlineRole t where t.id.accountOutlineId != ?1")
    List<Integer> findRoleIdListByAccountOutlineIdIsNot(Long accountOutlineId);

    /**
     * 查询某角色关联的帐号Id列表
     *
     * @param roleId 角色Id
     * @return 角色关联的帐号Id列表
     */
    @Query("select t.id.accountOutlineId from AccountOutlineRole t where t.id.roleId = ?1")
    List<Long> findAccountOutlineIdListByRoleId(Integer roleId);


    /**
     * 统计某帐号关联的角色个数
     *
     * @param accountOutlineId 帐号Id
     * @return 帐号关联角色个数
     */
    int countRolesByAccountOutlineId(Long accountOutlineId);

    @Query("select count(*) from (select DISTINCT t.id.roleId as roleId from AccountOutlineRole t where t.id.accountOutlineId != ?1) as T")
    int countRoleByNotAccountOutlineId(Long accountOutlineId);

    /**
     * 统计某角色关联的帐号个数
     *
     * @param roleId
     * @return
     */
    int countAccountOutlinesByRoleId(Integer roleId);

    /**
     * 统计没有和某角色关联的帐号个数
     *
     * @param roleId
     */
    @Query("select count(*) from (select distinct t.id.accountOutlineId as accountId from AccountOutlineRole t where t.id.roleId != ?1) as p")
    int countAccountOutlinesByNotRoleId(Integer roleId);

    int deleteByAccountOutlineId(Long accountOutlineId);

    int deleteByRoleId(Integer roleId);
}