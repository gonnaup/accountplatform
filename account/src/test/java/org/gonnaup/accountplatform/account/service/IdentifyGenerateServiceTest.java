package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author gonnaup
 * @version created at 2023/6/29 下午11:20
 */
@SpringBootTest
class IdentifyGenerateServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyGenerateServiceTest.class);

    final IdentifyGenerateService identifyGenerateService;

    final IdentifyStepService identifyStepService;

    IdentifyStep insertRandomStep(int step) {
        IdentifyStep identifyStep;
        do {
            identifyStep = new IdentifyStep(new Random().nextInt(100000, Integer.MAX_VALUE), UUID.randomUUID().toString(),
                    10000L, step, "", LocalDateTime.now(), LocalDateTime.now());
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
    void generateLongIdentify() throws InterruptedException {
        final int step = 10;
        final int stepNum = 3;
        List<IdentifyStep> identifySteps = new ArrayList<>();
        List<List<Long>> idsList = new ArrayList<>();
        for (int i = 0; i < stepNum; i++) {
            identifySteps.add(insertRandomStep(step));
            idsList.add(Collections.synchronizedList(new ArrayList<>()));
        }
        final int threadNumPerStep = 5;
        final int threadNum = threadNumPerStep * stepNum;
        final int numPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threadNum);

        for (int t = 0; t < threadNum; t++) {
            final int loc = t / threadNumPerStep;
            Thread thread = new Thread(() -> {
                for (int i = 0; i < numPerThread; i++) {
                    long lastId = identifyGenerateService.generateLongIdentify(identifySteps.get(loc).getId());
                    if (idsList.get(loc).contains(lastId)) {
                        logger.warn("ID段 ID={} 生成重复ID => {}", identifySteps.get(loc).getId(), lastId);
                    }
                    idsList.get(loc).add(lastId);
                }
                latch.countDown();
            });
            thread.setName("ID-Generator-test-" + (t + 1));
            thread.start();
        }

        latch.await();
        //
        Assertions.assertEquals(threadNum * numPerThread, idsList.stream().mapToInt(value -> value.size()).sum());
        idsList.forEach(longs -> Assertions.assertEquals(longs.size(), new HashSet<>(longs).size()));
        identifySteps.forEach(identifyStep -> identifyStepService.deleteIdentifyStep(identifyStep.getId()));
    }


    @Test
    void generateLongIdentifyBenchmark_nInentifyStep() throws InterruptedException {
        final int step = 100;
        final int idStepNum = 5;
        List<IdentifyStep> identifySteps = new ArrayList<>(idStepNum);
        for (int i = 0; i < 5; i++) {
            identifySteps.add(insertRandomStep(step));
        }
        final int threadNumPerStep = 5;//每段的线程数
        final int threadNum = idStepNum * threadNumPerStep;
        final int numPerThread = 10000;
        CountDownLatch latch = new CountDownLatch(threadNum);

        List<Long> ids = Collections.synchronizedList(new ArrayList<>(20000));

        long begin = System.currentTimeMillis();
        for (int t = 0; t < threadNum; t++) {
            int step_index = t / threadNumPerStep; //计算Step位置
            Thread thread = new Thread(() -> {
                for (int i = 0; i < numPerThread; i++) {
                    long lastId = identifyGenerateService.generateLongIdentify(identifySteps.get(step_index).getId());
                    ids.add(lastId);
                }
                latch.countDown();
            });
            thread.setName("ID-Generator-test-" + (t + 1));
            thread.start();
        }

        latch.await();

        long cost = System.currentTimeMillis() - begin;
        long total = threadNum * numPerThread;
        long numPerMill = total / cost;

        logger.info("{}条线程，每线程生成{}个，{}个ID段对象，每个段对象有{}条线程获取ID，step={}，性能：用时{}毫秒，每毫秒生成ID数 {}", threadNum, numPerThread, idStepNum, threadNumPerStep, step, cost, numPerMill);

        // clear
        identifySteps.forEach(identifyStep -> identifyStepService.deleteIdentifyStep(identifyStep.getId()));
    }
}