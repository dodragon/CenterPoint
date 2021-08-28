package com.dod.centerpoint.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerviewPagingListener extends RecyclerView.OnScrollListener {

    private boolean isScrollDown = false;
    private double dataRange = 0.0;
    private double dataExtend = 0.0;

    private final boolean isVertical;

    public RecyclerviewPagingListener(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public RecyclerviewPagingListener() {
        isVertical = true;
    }

    public abstract void onScrollChanged(RecyclerView recyclerView, int newState, boolean isAdding);

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        /* 스크롤이 아래 혹은 오른쪽 방향 이면서, 현재 보이는 리스트 개수가 총 리스트의 70%이상인 경우 */
        int offset;
        if (isVertical) {
            offset = recyclerView.computeVerticalScrollOffset();
        } else {
            offset = recyclerView.computeHorizontalScrollOffset();
        }

        if (isScrollDown && offset >= (dataRange - dataExtend) * 0.7) {
            onScrollChanged(recyclerView, newState, true);
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (isVertical) {
            isScrollDown = dy > 0;
            dataRange = recyclerView.computeVerticalScrollRange();
            dataExtend = recyclerView.computeVerticalScrollExtent();
        } else {
            isScrollDown = dx > 0;
            dataRange = recyclerView.computeHorizontalScrollRange();
            dataExtend = recyclerView.computeHorizontalScrollExtent();
        }
    }
}
