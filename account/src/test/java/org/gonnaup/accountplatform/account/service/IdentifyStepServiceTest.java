package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.gonnaup.accountplatform.account.repository.IdentifyStepRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gonnaup
 * @version created at 2023/6/29 下午7:02
 */
@SpringBootTest
class IdentifyStepServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyStepServiceTest.class);

    final IdentifyStepService identifyStepService;

    final IdentifyStepRepository identifyStepRepository;

    @Autowired
    IdentifyStepServiceTest(IdentifyStepService identifyStepService, IdentifyStepRepository identifyStepRepository) {
        this.identifyStepService = identifyStepService;
        this.identifyStepRepository = identifyStepRepository;
    }

    IdentifyStep insertRandomStep() {
        IdentifyStep identifyStep;
        do {
            identifyStep = new IdentifyStep(new Random().nextInt(100000, Integer.MAX_VALUE), UUID.randomUUID().toString(),
                    10000L, 100, "", LocalDateTime.now(), LocalDateTime.now());
        } while (identifyStepService.idExists(identifyStep.getId()));
        identifyStepService.addIdentifyStep(identifyStep);
        logger.info("添加测试ID段数据 {}", identifyStep);
        return identifyStep;
    }

    IdentifyStep obtainRandomStep() {
        IdentifyStep identifyStep;
        do {

            identifyStep = new IdentifyStep(new Random().nextInt(100000, Integer.MAX_VALUE), UUID.randomUUID().toString(),
                    10000L, 100, "", LocalDateTime.now(), LocalDateTime.now());
        } while (identifyStepService.idExists(identifyStep.getId()));
        return identifyStep;
    }

    @Test
    @Transactional
    void nameExists() {
        IdentifyStep identifyStep = insertRandomStep();
        assertTrue(identifyStepService.nameExists(identifyStep.getIdentifyName()));
    }

    @Test
    @Transactional
    void findById() {
        IdentifyStep identifyStep = insertRandomStep();
        assertNotNull(identifyStepService.findById(identifyStep.getId()));
    }

    @Test
    @Transactional
    void findByName() {
        IdentifyStep identifyStep = insertRandomStep();
        assertNotNull(identifyStepService.findByName(identifyStep.getIdentifyName()));
    }

    @Test
    @Transactional
    void findIdentifyStepsPageable() {
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        insertRandomStep();
        GenericPage<IdentifyStep> recordPage = identifyStepService.findIdentifyStepsPageable(null, PageRequest.of(0, 5));
        assertEquals(recordPage.getRecords().size(), 5);
        assertTrue(recordPage.getTotalElements() >= 10);
        assertTrue(recordPage.getTotalPages() >= 2);
    }

    @Test
    @Transactional
    void addIdentifyStep() {
        IdentifyStep identifyStep = insertRandomStep();
        Assertions.assertNotNull(identifyStepService.findById(identifyStep.getId()));
    }

    @Test
    @Transactional
    void updateIdentifyStep() {
        IdentifyStep identifyStep = insertRandomStep();
        String modifyName = UUID.randomUUID().toString();
        identifyStep.setIdentifyName(modifyName);
        identifyStep.setIdentifyInterval(50);
        String des = UUID.randomUUID().toString();
        identifyStep.setDescription(des);
        identifyStepService.updateIdentifyStep(identifyStep);

        IdentifyStep afterUpdate = identifyStepService.findById(identifyStep.getId());
        assertEquals(afterUpdate.getIdentifyName(), modifyName);
        assertEquals(afterUpdate.getIdentifyInterval(), 50);
        assertEquals(afterUpdate.getDescription(), des);
    }

    @Test
    @Transactional
    void deleteIdentifyStep() {
        IdentifyStep identifyStep = insertRandomStep();
        IdentifyStep beforeDel = identifyStepService.deleteIdentifyStep(identifyStep.getId());
        assertEquals(identifyStep.getIdentifyName(), beforeDel.getIdentifyName());
        assertNull(identifyStepService.findById(identifyStep.getId()));
    }

    @Test
    @Transactional
    void nextStep() {
        IdentifyStep identifyStep = insertRandomStep();
        for (int i = 0; i < 5; i++) {
            identifyStepService.nextStep(identifyStep.getId());
        }
        assertEquals(identifyStep.getIdentifyBegin() + identifyStep.getIdentifyInterval() * 5L, identifyStepService.findById(identifyStep.getId()).getIdentifyBegin());
    }

    @Test
    @Transactional
    void idExists() {
        IdentifyStep identifyStep = insertRandomStep();
        assertTrue(identifyStepService.idExists(identifyStep.getId()));
    }


}