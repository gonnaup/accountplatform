package org.gonnaup.accountplatform.account.repository;

import org.gonnaup.accountplatform.account.entity.AccountAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountAuthenticationRepository extends JpaRepository<AccountAuthentication, Long> {
}