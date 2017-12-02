package com.yjt.wallet.components.widget.tablayout.listener;

/**
 * Interface definition for a callback to be invoked when the scroll position of a view changes.
 */
public interface OnScrollChangeListener {

    /**
     * Called when the scroll position of a view changes.
     *
     * @param scrollX    Current horizontal scroll origin.
     * @param oldScrollX Previous horizontal scroll origin.
     */
    void onScrollChanged(int scrollX, int oldScrollX);
}
