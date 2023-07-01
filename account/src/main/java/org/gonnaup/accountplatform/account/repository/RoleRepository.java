package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}