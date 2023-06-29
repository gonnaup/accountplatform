package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

/**
 * @author gonnaup
 * @version created at 2023/6/28 下午11:38
 */
@MappedSuperclass
public abstract class TimedEntity {
    @Column(name = "create_time")
    protected LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
