package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.constant.BindType;
import org.gonnaup.accountplatform.account.entity.AccountBind;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/7/19 12:39
 */
@SpringBootTest
class AccountBindServiceTest {

    AccountBindService accountBindService;

    AccountOutlineService accountOutlineService;

    @Autowired
    public AccountBindServiceTest(AccountBindService accountBindService, AccountOutlineService accountOutlineService) {
        this.accountBindService = accountBindService;
        this.accountOutlineService = accountOutlineService;
    }

    @Test
    void testAccountBindService() {
        AccountOutline a1 = new AccountOutline();
        a1.setAccountName(RandomUtil.randomAlphabet(12));
        a1.setPersonalSignature(RandomUtil.randomString(58));
        a1.setRegion("1000100");
        a1 = accountOutlineService.addAccountOutline(a1);

        AccountBind b1 = new AccountBind();
        b1.setAccountId(a1.getId());
        b1.setBindType(BindType.Email.value);
        b1.setPrincipal(RandomUtil.randomAlphabet(8) + "@gmail.com");

        accountBindService.addAccountBind(b1);
        assertEquals(b1.getPrincipal(), accountBindService.findAccountBindById(b1.getId()).getPrincipal());

        AccountBind b2 = new AccountBind();
        b2.setAccountId(a1.getId());
        b2.setBindType(BindType.Phone.value);
        b2.setPrincipal(RandomUtil.randomNumbericString(11));
        accountBindService.addAccountBind(b2);

        assertEquals(2, accountBindService.findAccountBindByAccountId(a1.getId()).size());
        assertNotNull(accountBindService.findAccountBindByBindTypeAndPrincipal(BindType.Phone, b2.getPrincipal()));
        assertNull(accountBindService.findAccountBindByBindTypeAndPrincipal(BindType.Email, null));

        String newPrincipal = RandomUtil.randomNumbericString(11);
        String newToken = RandomUtil.randomString(36);
        b2.setPrincipal(newPrincipal);
        b2.setToken(newToken);
        accountBindService.updateAccountBind(b2);

        AccountBind b2_ = accountBindService.findAccountBindById(b2.getId());
        assertEquals(newPrincipal, b2_.getPrincipal());
        assertEquals(newToken, b2_.getToken());
        assertEquals(b2.getBindType(), b2_.getBindType());

        accountBindService.deleteAccountBindById(b1.getId());
        assertNull(accountBindService.findAccountBindById(b1.getId()));
        assertEquals(1, accountBindService.findAccountBindByAccountId(a1.getId()).size());


    }
}