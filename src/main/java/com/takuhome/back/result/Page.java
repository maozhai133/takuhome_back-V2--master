package com.takuhome.back.result;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author nekotaku
 * @create 2021-08-07 18:21
 */
@Slf4j
@Data
public class Page implements Serializable {

    private static final long serialVersionUID = 7326507104519618467L;

    private Integer page;//当前页
    private Integer limit;//每页显示多少条
    private Integer offset;//数据库偏移量

    public void countOffset() {
        if (this.page == null || this.limit == null){
            this.offset = 0;
            return;
        }
        this.offset = (this.page - 1) * this.limit;
    }

    @Override
    public String toString() {
        return "PageTableRequest{" +
                "page=" + page +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }

}
