package nl.finan.finq.rest;

import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<T> {

    private List<T> data;
    private Long totalCount;
    private Integer page;
    private Integer pageSize;
    private Map<String, String> _link = new HashMap<>();

    public Page(List<T> resultList, Long count, Integer page, Integer size, UriInfo uriInfo) {
        data = resultList;
        totalCount = count;
        this.page = page;
        pageSize = size;
        if ((page + 1) * size < count) {
            _link.put("next", uriInfo.getRequestUriBuilder().replaceQueryParam("page", page + 1).build().toString());
        }
        if (page > 0) {
            _link.put("previous", uriInfo.getRequestUriBuilder().replaceQueryParam("page", page - 1).build().toString());
        }
    }

    public List<T> getData() {
        return data;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Map<String, String> get_link() {
        return _link;
    }
}
