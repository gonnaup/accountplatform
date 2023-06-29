package org.gonnaup.accountplatform.account.service.impl;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.gonnaup.accountplatform.account.exception.RecordNotExistException;
import org.gonnaup.accountplatform.account.repository.IdentifyStepRepository;
import org.gonnaup.accountplatform.account.service.IdentifyStepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author gonnaup
 * @version created at 2023/6/29 下午2:25
 */
@Service
public class IdentifyStepServiceImpl implements IdentifyStepService {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyStepServiceImpl.class);

    private final IdentifyStepRepository identifyStepRepository;

    @Autowired
    public IdentifyStepServiceImpl(IdentifyStepRepository identifyStepRepository) {
        this.identifyStepRepository = identifyStepRepository;
    }

    /**
     * 添加ID段
     *
     * @param identifyStep
     * @return
     */
    @Override
    @Transactional
    public IdentifyStep addIdentifyStep(IdentifyStep identifyStep) {
        if (identifyStep.getIdentifyBegin() <= 0 || identifyStep.getIdentifyInterval() <= 0) {
            logger.error("新增ID段 identifyBegin、identifyInterval字段必须大于0，当前分别为 {} {}", identifyStep.getIdentifyBegin(), identifyStep.getIdentifyInterval());
            throw new IllegalArgumentException("新增ID段 identifyBegin、identifyInterval字段必须大于0");
        }
        if (identifyStep.getCreateTime() == null) {
            identifyStep.setCreateTime(LocalDateTime.now());
        }
        identifyStepRepository.save(identifyStep);
        logger.info("添加 ID段 {} 成功", identifyStep);
        return null;
    }

    /**
     * 更新 ID段，只能更新字段<code>identifyName,identifyStep, description</code>
     *
     * @param identifyStep
     * @return
     */
    @Override
    @Transactional
    public IdentifyStep updateIdentifyStep(IdentifyStep identifyStep) {
        Optional<IdentifyStep> idStep = identifyStepRepository.findById(identifyStep.getId());
        if (idStep.isEmpty()) {
            throw new RecordNotExistException(String.format("数据ID=[%s]不存在", identifyStep.getId()));
        }
        if (identifyStep.getIdentifyInterval() <= 0) {
            logger.error("修改ID段 identifyInterval字段必须大于0，当前分别 {}", identifyStep.getIdentifyInterval());
            throw new IllegalArgumentException("修改ID段 identifyInterval字段必须大于0");
        }
        IdentifyStep idStepStored = idStep.get();
        idStepStored.setIdentifyName(identifyStep.getIdentifyName());
        idStepStored.setIdentifyInterval(identifyStep.getIdentifyInterval());
        idStepStored.setDescription(identifyStep.getDescription());
        identifyStepRepository.save(idStepStored);
        logger.info("更新 ID = {} 的ID段", identifyStep.getId());
        return idStepStored;
    }

    /**
     * 删除 ID段
     *
     * @param stepId
     * @return
     */
    @Override
    @Transactional
    public IdentifyStep deleteIdentifyStep(Integer stepId) {
        Optional<IdentifyStep> identifyStepOptional = identifyStepRepository.findById(stepId);
        if (identifyStepOptional.isEmpty()) {
            logger.warn("要删除的ID段 ID=[{}] 不存在", stepId);
            return null;
        }
        identifyStepRepository.deleteById(stepId);
        IdentifyStep identifyStep = identifyStepOptional.get();
        logger.info("删除 ID段 {}", identifyStep);
        return identifyStep;
    }

    /**
     * 获取段并更新数据库段
     *
     * @param stepId
     * @return 最新ID段
     */
    @Override
    @Transactional
    public IdentifyStep nextStep(Integer stepId) {
        //获取数据库段数据
        IdentifyStep identifyStep = identifyStepRepository.findById(stepId)
                .orElseThrow(() -> new RecordNotExistException(String.format("IdentifyStep ID=[%d] not exist", stepId)));
        //更新数据库段数据
        identifyStepRepository.toNextIdentifyStep(stepId);
        if (logger.isDebugEnabled()) {
            logger.debug("获取 [{}] 下一个ID段 {}", stepId, identifyStep);
        }
        return identifyStep;
    }

    /**
     * 段ID是否存在
     *
     * @param stepId
     * @return
     */
    @Override
    public boolean idExists(Integer stepId) {
        return identifyStepRepository.existsById(stepId);
    }

    /**
     * 段名称是否存在
     *
     * @param name
     * @return
     */
    @Override
    public boolean nameExists(String name) {
        return identifyStepRepository.existsByIdentifyName(name);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public IdentifyStep findById(Integer id) {
        return identifyStepRepository.findById(id).orElse(null);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public IdentifyStep findByName(String name) {
        return identifyStepRepository.findByIdentifyName(name).orElse(null);
    }

    /**
     * 分页查询，忽略创建时间字段
     *
     * @param pageable
     * @return
     */
    @Override
    public GenericPage<IdentifyStep> findIdentifyStepsPageable(IdentifyStep example, Pageable pageable) {
        if (example == null) {
            return GenericPage.fromPage(identifyStepRepository.findAll(pageable));
        }
        ExampleMatcher.matching()
                .withMatcher("id", matcher -> matcher.contains())
                .withMatcher("identifyName", matcher -> matcher.contains())
                .withMatcher("description", matcher -> matcher.contains())
                .withIgnorePaths("createTime"); // 忽略创建时间查询条件
        return GenericPage.fromPage(identifyStepRepository.findAll(Example.of(example), pageable));
    }
}
