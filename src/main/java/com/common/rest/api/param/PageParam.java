package com.common.rest.api.param;

import java.util.List;

/**
 * description: PageParam <br>
 * date: 2019/9/2 16:44 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */
public class PageParam {
    private int page = 1;
    private int pageSize = 10;
    private String search;
    List<SortParam> sorts;
    List<SearchParam> searchs;

    public PageParam() {
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearch() {
        return this.search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<SortParam> getSorts() {
        return this.sorts;
    }

    public void setSorts(List<SortParam> sorts) {
        this.sorts = sorts;
    }

    public List<SearchParam> getSearchs() {
        return this.searchs;
    }

    public void setSearchs(List<SearchParam> searchs) {
        this.searchs = searchs;
    }
}
