package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.entity.AccountAuthentication;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/7/19 12:14
 */
@SpringBootTest
class AccountAuthenticationServiceTest {

    AccountAuthenticationService accountAuthenticationService;

    AccountOutlineService accountOutlineService;

    @Autowired
    public AccountAuthenticationServiceTest(AccountAuthenticationService accountAuthenticationService, AccountOutlineService accountOutlineService) {
        this.accountAuthenticationService = accountAuthenticationService;
        this.accountOutlineService = accountOutlineService;
    }


    @Test
    void testAccountAuthenticationService() {
        AccountOutline a1 = new AccountOutline();
        a1.setAccountName(RandomUtil.randomAlphabet(12));
        a1.setPersonalSignature(RandomUtil.randomString(58));
        a1.setRegion("1000100");
        a1 = accountOutlineService.addAccountOutline(a1);

        AccountAuthentication aa1 = AccountAuthentication.of(a1.getId(), Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));

        accountAuthenticationService.addAccountAuthentication(aa1);
        assertNotNull(accountAuthenticationService.findByAccountId(a1.getId()));

        String passwordNew = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        aa1.setCredential(passwordNew);
        accountAuthenticationService.updateAccountAuthentication(aa1);
        assertEquals(passwordNew, accountAuthenticationService.findByAccountId(aa1.getAccountId()).getCredential());

        accountAuthenticationService.deleteAccountAuthenticationById(aa1.getAccountId());
        assertNull(accountAuthenticationService.findByAccountId(aa1.getAccountId()));

        // save to test db
        accountAuthenticationService.addAccountAuthentication(aa1);

    }
}