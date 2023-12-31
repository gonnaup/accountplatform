package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

/**
 * 具有创建时间和更新时间的公共实体类
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午11:38
 */
@MappedSuperclass
public abstract class TimedEntity {
    @Column(name = "create_time")
    protected LocalDateTime createTime;

    @Column(name = "update_time")
    protected LocalDateTime updateTime;

    /**
     * 将创建时间和更新时间都设置成现在时间
     */
    public void setBothTimeToNow() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

    /**
     * 将更新时间设置成现在时间
     */
    public void setUpdateTimeToNow() {

    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
