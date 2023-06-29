package org.gonnaup.accountplatform.account.service.impl;

import jakarta.annotation.PostConstruct;
import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.gonnaup.accountplatform.account.service.IdentifyGenerateService;
import org.gonnaup.accountplatform.account.service.IdentifyStepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gonnaup
 * @version created at 2023/6/29 下午12:42
 */
@Service
public class IdentifyGenerateServiceImpl implements IdentifyGenerateService {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyGenerateServiceImpl.class);

    private final IdentifyStepService identifyStepService;

    private final ConcurrentHashMap<Integer, IdentifyGenerator> generatorStore = new ConcurrentHashMap<>(2);


    public IdentifyGenerateServiceImpl(IdentifyStepService identifyStepService) {
        this.identifyStepService = identifyStepService;
    }

    @Override
    public Long generateLongIdentify(Integer stepId) {
        IdentifyGenerator generator = generatorStore.get(stepId);
        if (generator == null) {
            // 此处并发问题在于可能后现场覆盖前面的段数据，可能丢失一段ID，无实际影响
            generator = IdentifyGenerator.of(identifyStepService.nextStep(stepId));
            generatorStore.put(stepId, generator);
        }

        long id = generator.generator.getAndIncrement();
        //判断当前值是否超过最大值
        while (id >= generator.maxVal) {
            //在高并发下，由于此处请求数据库较慢，将导致generator重复更新而丢失多段ID
            generator = IdentifyGenerator.of(identifyStepService.nextStep(stepId));
            generatorStore.put(stepId, generator);
            id = generator.generator.getAndIncrement();
        }
        return id;
    }

    @Override
    public Integer generateIntegerIdentify(Integer stepId) {
        IdentifyGenerator generator = generatorStore.get(stepId);
        if (generator == null) {
            // 此处并发问题在于可能后现场覆盖前面的段数据，可能丢失一段ID，无实际影响
            generator = IdentifyGenerator.of(identifyStepService.nextStep(stepId));
            generatorStore.put(stepId, generator);
        }

        long id = generator.generator.getAndIncrement();
        //判断当前值是否超过最大值
        while (id >= generator.maxVal) {
            //在高并发下，由于此处请求数据库较慢，将导致generator重复更新而丢失多段ID
            generator = IdentifyGenerator.of(identifyStepService.nextStep(stepId));
            id = generator.generator.getAndIncrement();
        }
        return (int) id;
    }

    /**
     * 初始化本系统需要的ID段数据
     */
    @PostConstruct
    @Transactional
    public void initApplicationIdentifyStep() {
        logger.info("开始初始化ID段 IdentifyStep 数据...");
        Optional<IdentifyStep> accountStepOptional = Optional.ofNullable(identifyStepService.findById(IdentifyGenerateService.ACCOUNT_STEP_ID));
        if (accountStepOptional.isEmpty()) {
            IdentifyStep accountStep = new IdentifyStep(IdentifyGenerateService.ACCOUNT_STEP_ID, IdentifyGenerateService.ACCOUNT_STEP_NAME
                    , 10000L, 100, "帐号相关ID段", LocalDateTime.now());
            identifyStepService.addIdentifyStep(accountStep);
            logger.info("添加帐号ID段 => {}", accountStep);
        } else {
            IdentifyStep accountStep = accountStepOptional.get();
            logger.info("已存在帐号ID段 => {}", accountStep);
        }

        Optional<IdentifyStep> authStepOptional = Optional.ofNullable(identifyStepService.findById(IdentifyGenerateService.AUTH_STEP_ID));
        if (authStepOptional.isEmpty()) {
            IdentifyStep authStep = new IdentifyStep(IdentifyGenerateService.AUTH_STEP_ID, IdentifyGenerateService.AUTH_STEP_NAME
                    , 10000L, 100, "认证相关ID段", LocalDateTime.now());
            identifyStepService.addIdentifyStep(authStep);
            logger.info("添加认证ID段 => {}", authStep);
        } else {
            IdentifyStep authStep = authStepOptional.get();
            logger.info("已经存在认证习惯ID段");
        }
        logger.info("初始化ID段完成...");
    }

    private static class IdentifyGenerator {

        private AtomicLong generator;

        private Long maxVal;

        static IdentifyGenerator of(IdentifyStep identifyStep) {
            IdentifyGenerator g = new IdentifyGenerator();
            g.generator = new AtomicLong(identifyStep.getIdentifyBegin());
            g.maxVal = identifyStep.getIdentifyBegin() + identifyStep.getIdentifyInterval();
            return g;
        }


    }

}
