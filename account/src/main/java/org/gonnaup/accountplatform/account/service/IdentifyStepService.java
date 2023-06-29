package org.gonnaup.accountplatform.account.service;

import org.gonnaup.accountplatform.account.domain.GenericPage;
import org.gonnaup.accountplatform.account.entity.IdentifyStep;
import org.springframework.data.domain.Pageable;

/**
 * ID段服务对象
 *
 * @author gonnaup
 * @version created at 2023/6/29 下午1:20
 */
public interface IdentifyStepService {

    /**
     * 添加ID段
     *
     * @param
     * @return
     */
    IdentifyStep addIdentifyStep(IdentifyStep identifyStep);

    /**
     * 更新 ID段，只能更新字段<code>identifyName, identifyInterval, description</code>
     *
     * @param identifyStep
     * @return
     */
    IdentifyStep updateIdentifyStep(IdentifyStep identifyStep);

    /**
     * 删除 ID 段
     *
     * @param stepId
     * @return
     */
    IdentifyStep deleteIdentifyStep(Integer stepId);

    /**
     * 获取段并更新数据库段
     *
     * @param stepId
     * @return ID 段
     */
    IdentifyStep nextStep(Integer stepId);

    /**
     * 段ID是否存在
     *
     * @param stepId
     * @return ID存在返回true
     */
    boolean idExists(Integer stepId);

    /**
     * 段名称是否存在
     *
     * @param name
     * @return 名称存在返回true
     */
    boolean nameExists(String name);


    IdentifyStep findById(Integer id);


    IdentifyStep findByName(String name);


    /**
     * 分页查询，忽略创建时间字段
     *
     * @param example
     * @param pageable
     * @return
     */
    GenericPage<IdentifyStep> findIdentifyStepsPageable(IdentifyStep example, Pageable pageable);


}
