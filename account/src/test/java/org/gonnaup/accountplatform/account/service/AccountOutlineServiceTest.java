package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.constant.AccountState;
import org.gonnaup.accountplatform.account.constant.Gender;
import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.AccountOutline;
import org.gonnaup.common.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/6/30 上午12:08
 */
@SpringBootTest
class AccountOutlineServiceTest {

    AccountOutlineService accountOutlineService;


    @Autowired
    public AccountOutlineServiceTest(AccountOutlineService accountOutlineService) {
        this.accountOutlineService = accountOutlineService;
    }

    @Test
    void testAccountOutlineService() {
        AccountOutline account = new AccountOutline();
        account.setAccountName(RandomUtil.randomAlphabet(12));
        account.setPersonalSignature(RandomUtil.randomString(58));
        account.setRegion("1000100");
        accountOutlineService.addAccountOutline(account);
        Long id = account.getId();
        assertNotNull(accountOutlineService.findAccountOutlineByAccountId(id));
        assertNotNull(accountOutlineService.findAccountOutlineByAccountName(account.getAccountName()));
        assertTrue(accountOutlineService.accountNameExist(account.getAccountName()));

        String newName = RandomUtil.randomAlphabet(24);
        accountOutlineService.updateAccountName(id, newName);
        account = accountOutlineService.findAccountOutlineByAccountId(id);
        assertEquals(newName, account.getAccountName());

        accountOutlineService.updateState(id, AccountState.Locked);
        account = accountOutlineService.findAccountOutlineByAccountId(id);
        assertEquals(AccountState.Locked.value, account.getState());

        String newUrl = "http://gonnaup.com/static/JOlsdfdsoOKLSAlOOKJKJAIUJ.png";
        accountOutlineService.updateAvatarUrl(id, newUrl);
        account = accountOutlineService.findAccountOutlineByAccountId(id);
        assertEquals(newUrl, account.getAvatarUrl());


        String newNickName = RandomUtil.randomString(18);
        String gender = Gender.Male.value;
        AccountOutline update = new AccountOutline();
        update.setId(account.getId());
        update.setNickName(newNickName);
        update.setGender(gender);
        AccountOutline updated = accountOutlineService.updateAccountOutlineBrief(update);
        assertNotSame(account, updated);
        assertEquals(update.getNickName(), updated.getNickName());
        assertEquals(update.getGender(), updated.getGender());
        assertEquals(update.getRegion(), updated.getRegion());
        assertEquals(update.getPersonalSignature(), updated.getPersonalSignature());

        accountOutlineService.disableAccount(id);
        account = accountOutlineService.findAccountOutlineByAccountId(id);
        assertEquals(AccountState.Disabled.value, account.getState());

        accountOutlineService.removeAccount(id);
        account = accountOutlineService.findAccountOutlineByAccountId(id);
        assertEquals(AccountState.Removed.value, account.getState());

        AccountOutline a1 = new AccountOutline();
        a1.setNickName(RandomUtil.randomStringWithPrefix(18, "TEST_"));
        AccountOutline a2 = new AccountOutline();
        a2.setNickName(RandomUtil.randomStringWithPrefix(18, "TEST_"));
        AccountOutline a3 = new AccountOutline();
        a3.setNickName(RandomUtil.randomStringWithPrefix(18, "TEST_"));
        AccountOutline a4 = new AccountOutline();
        a4.setNickName(RandomUtil.randomStringWithPrefix(18, "TEST_"));

        accountOutlineService.addAccountOutline(a1);
        accountOutlineService.addAccountOutline(a2);
        accountOutlineService.addAccountOutline(a3);
        accountOutlineService.addAccountOutline(a4);

        AccountOutline example = new AccountOutline();
        example.setNickName("TEST_");
        GenericPage<AccountOutline> accountPage = accountOutlineService.findAccountOutlineListPaged(example, PageRequest.of(1, 2));
        assertEquals(2, accountPage.getRecords().size());
    }
}