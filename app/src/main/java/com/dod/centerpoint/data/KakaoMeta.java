package com.dod.centerpoint.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KakaoMeta implements Serializable {

    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("pageable_count")
    private int pageableCount;
    @SerializedName("is_end")
    private boolean isEnd;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageableCount() {
        return pageableCount;
    }

    public void setPageableCount(int pageableCount) {
        this.pageableCount = pageableCount;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "KakaoMeta{" +
                "totalCount=" + totalCount +
                ", pageableCount=" + pageableCount +
                ", isEnd=" + isEnd +
                '}';
    }
}
