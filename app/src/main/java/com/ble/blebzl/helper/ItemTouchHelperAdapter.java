package com.ble.blebzl.helper;

/**
 * Created by wyl on 2017/9/8.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
