package com.vn.backend.dto.response;


import java.util.List;

public class PagableResponse<T> {
    private List<T> list;
    private int total;

    public PagableResponse(List<T> list) {
        this.list = list;
        this.total = (list != null) ? list.size() : 0;
    }

    public List<T> getList() {
        return list;
    }

    public int getTotal() {
        return total;
    }

    public void setItems(List<T> list) {
        this.list = list;
        this.total = (list != null) ? list.size() : 0;
    }
}
