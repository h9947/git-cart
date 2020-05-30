package com.qf.commons;

import java.util.List;

/**
 * 该类主要目的是配合 bootstrap-table插件类构建前端的分页数据。
 * 因为bootstrap-table所需要的数据格式：
 *       {
 *           "total": 23,
 *           "rows": [{}, {}, {}]
 *       }
 *
 */
public class TableData<T> {
    private Integer total;
    private List<T> rows;

    public TableData() {
    }

    public TableData(Integer total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
