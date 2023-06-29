package org.gonnaup.accountplatform.account.domain;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 通用数据页
 *
 * @author gonnaup
 * @version created at 2023/6/29 下午1:32
 */
public class GenericPage<T> {

    /**
     * 分页数据
     */
    private List<T> records;

    /**
     * 分页总数
     */
    private int totalPages;

    /**
     * 数据总数
     */
    private long totalElements;


    public GenericPage(List<T> records, int totalPages, long totalElements) {
        this.records = records;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public static <T> GenericPage<T> fromPage(Page<T> page) {
        return new GenericPage<>(page.getContent(), page.getTotalPages(), page.getTotalElements());
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
