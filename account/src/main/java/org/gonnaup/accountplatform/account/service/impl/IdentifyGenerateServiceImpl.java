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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author gonnaup
 * @version created at 2023/6/29 下午12:42
 */
@Service
public class IdentifyGenerateServiceImpl implements IdentifyGenerateService {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyGenerateServiceImpl.class);

    private final IdentifyStepService identifyStepService;

    private final Map<Integer, IdentifyGenerator> generatorStore = new HashMap<>();


    public IdentifyGenerateServiceImpl(IdentifyStepService identifyStepService) {
        this.identifyStepService = identifyStepService;
    }

    /**
     * 生成 Long 类型ID
     *
     * @param stepId 段对象ID
     * @return
     */
    @Override
    public synchronized Long generateLongIdentify(Integer stepId) {
        IdentifyGenerator generator = generatorStore.get(stepId);
        if (generator == null) {
            generator = IdentifyGenerator.of(identifyStepService.nextStep(stepId));
            generatorStore.put(stepId, generator);
        }

        long id = generator.getAndIncrement();
        //判断当前值是否超过最大值
        while (id >= generator.maxVal) {
            generator = IdentifyGenerator.of(identifyStepService.nextStep(stepId));
            generatorStore.put(stepId, generator);
            id = generator.getAndIncrement();
        }
        return id;
    }

    /**
     * 生成 Integer 类型ID
     *
     * @param stepId 段对象ID
     * @return
     */
    @Override
    public Integer generateIntegerIdentify(Integer stepId) {
        Long id = this.generateLongIdentify(stepId);
        return id.intValue();
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

        private long generator;

        private long maxVal;

        static IdentifyGenerator of(IdentifyStep identifyStep) {
            IdentifyGenerator g = new IdentifyGenerator();
            g.generator = identifyStep.getIdentifyBegin();
            g.maxVal = identifyStep.getIdentifyBegin() + identifyStep.getIdentifyInterval();
            return g;
        }

        long getAndIncrement() {
            return generator++;
        }

    }

}
