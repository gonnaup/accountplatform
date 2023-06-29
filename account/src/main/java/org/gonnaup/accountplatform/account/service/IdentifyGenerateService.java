package org.gonnaup.accountplatform.account.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * ID 生成服务接口
 *
 * @author gonnaup
 * @version created at 2023/6/29 下午12:07
 */
public interface IdentifyGenerateService {
    // 帐号段ID
    Integer ACCOUNT_STEP_ID = 100;

    String ACCOUNT_STEP_NAME = "AP_ACCOUNT_STEP";

    // 认证段ID
    Integer AUTH_STEP_ID = 200;

    String AUTH_STEP_NAME = "AP_AUTH_STEP";

    /**
     * 生成 Long 类型ID
     *
     * @param stepId 段对象ID
     * @return
     */
    Long generateLongIdentify(Integer stepId);

    /**
     * 生成 Integer 类型ID
     *
     * @param stepId 段对象ID
     * @return
     */
    Integer generateIntegerIdentify(Integer stepId);


    @Transactional
    default Long generateAccountId() {
        return generateLongIdentify(ACCOUNT_STEP_ID);
    }

    @Transactional
    default Integer generateAuthId() {
        return generateIntegerIdentify(AUTH_STEP_ID);
    }

}
