package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.AccountBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountBindRepository extends JpaRepository<AccountBind, Long> {

    List<AccountBind> findByAccountId(Long accountId);
    
    AccountBind findByBindTypeAndPrincipal(String bindType, String principal);

}