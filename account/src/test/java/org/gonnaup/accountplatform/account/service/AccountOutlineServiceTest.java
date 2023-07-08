package org.gonnaup.accountplatform.account.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gonnaup
 * @version created at 2023/6/30 上午12:08
 */
@SpringBootTest
class AccountOutlineServiceTest {

    final AccountOutlineService accountOutlineService;

    @Autowired
    AccountOutlineServiceTest(AccountOutlineService accountOutlineService) {
        this.accountOutlineService = accountOutlineService;
    }

    @Test
    void test1() {
    }
}