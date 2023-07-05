package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.AccountAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountAuthenticationRepository extends JpaRepository<AccountAuthentication, Long> {

    @Query("from AccountAuthentication a where a.authenticationType = ?1 and a.principal = ?2")
    Optional<AccountAuthentication> findByAuthenticationTypeAndPrincipal(String type, String principal);

    List<AccountAuthentication> findByAccountId(Long accountId);

    @Query("from AccountAuthentication a where a.authenticationType = ?2 and a.accountId = ?1")
    List<AccountAuthentication> findByAccountIdAndAuthenticationType(Long accountId, String authenticationType);


}