package org.gonnaup.accountplatform.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * ID 生成器的ID段对象，该对象可生成 >= identifyBegin & < identifyBegin + identifyInterval之间的id
 *
 * @author gonnaup
 * @version created at 2023/6/28 下午10:15
 */
@Entity
@Table(name = "t_identify_step")
public class IdentifyStep extends TimedEntity {

    /**
     * 对象ID
     */
    @Id
    private Integer id;

    /**
     * ID 段名称
     */
    @Column(name = "identify_name", length = 50, nullable = false, unique = true)
    private String identifyName;

    /**
     * ID 段开始值
     */
    @Column(name = "identify_begin", nullable = false)
    private Long identifyBegin;

    /**
     * ID 段间隔
     */
    @Column(name = "identify_interval", nullable = false)
    private Integer identifyInterval;

    /**
     * 描述
     */
    @Column(length = 500)
    private String description;

    public IdentifyStep() {
    }

    public IdentifyStep(Integer id, String identifyName, Long identifyBegin, Integer identifyInterval, String description, LocalDateTime createTime) {
        this.id = id;
        this.identifyName = identifyName;
        this.identifyBegin = identifyBegin;
        this.identifyInterval = identifyInterval;
        this.description = description;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IdentifyStep.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("identifyName='" + identifyName + "'")
                .add("identifyBegin=" + identifyBegin)
                .add("identifyInterval=" + identifyInterval)
                .add("description='" + description + "'")
                .toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentifyName() {
        return identifyName;
    }

    public void setIdentifyName(String identifyName) {
        this.identifyName = identifyName;
    }

    public Long getIdentifyBegin() {
        return identifyBegin;
    }

    public void setIdentifyBegin(Long identifyBegin) {
        this.identifyBegin = identifyBegin;
    }

    public Integer getIdentifyInterval() {
        return identifyInterval;
    }

    public void setIdentifyInterval(Integer identifyInterval) {
        this.identifyInterval = identifyInterval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
