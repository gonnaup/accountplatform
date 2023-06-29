package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.service.AccountOutlineService;
import org.gonnaup.accountplatform.account.service.IdentifyGenerateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gonnaup
 * @version created at 2023/6/30 上午12:05
 */
@Service
public class AccountOutlineServiceImpl implements AccountOutlineService {

    private static final Logger logger = LoggerFactory.getLogger(AccountOutlineServiceImpl.class);

    private final IdentifyGenerateService identifyGenerateService;

    @Autowired
    public AccountOutlineServiceImpl(IdentifyGenerateService identifyGenerateService) {
        this.identifyGenerateService = identifyGenerateService;
    }

    @Override
    @Transactional
    public void test() {
        for (int i = 0; i < 10000; i++) {
            Long id = identifyGenerateService.generateAccountId();
            logger.info("generate account id {}", id);
        }
    }
}
