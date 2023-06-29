package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * @author gonnaup
 * @version created at 2023/6/29 下午11:20
 */
@SpringBootTest
class IdentifyGenerateServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyGenerateServiceTest.class);

    final IdentifyGenerateService identifyGenerateService;

    final IdentifyStepService identifyStepService;

    IdentifyStep insertRandomStep() {
        IdentifyStep identifyStep;
        do {
            identifyStep = new IdentifyStep(new Random().nextInt(100000, Integer.MAX_VALUE), UUID.randomUUID().toString(),
                    10000L, 10, "", LocalDateTime.now());
        } while (identifyStepService.idExists(identifyStep.getId()));
        identifyStepService.addIdentifyStep(identifyStep);
        logger.info("添加测试ID段数据 {}", identifyStep);
        return identifyStep;
    }

    @Autowired
    public IdentifyGenerateServiceTest(IdentifyGenerateService identifyGenerateService, IdentifyStepService identifyStepService) {
        this.identifyGenerateService = identifyGenerateService;
        this.identifyStepService = identifyStepService;
    }

    @Test
    @Transactional
    void generateLongIdentify() {
        IdentifyStep identifyStep = insertRandomStep();
        long lastId = 0;
        for (int i = 0; i < 1000; i++) {
            lastId = identifyGenerateService.generateLongIdentify(identifyStep.getId());
            logger.info(String.valueOf(lastId));
        }
        Assertions.assertEquals(identifyStep.getIdentifyBegin() + 1000, identifyStepService.findById(identifyStep.getId()).getIdentifyBegin());
    }

    @Test
    void generateIntegerIdentify() {
    }
}