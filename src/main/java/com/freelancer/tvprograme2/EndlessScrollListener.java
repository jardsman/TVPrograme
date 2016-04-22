package com.freelancer.tvprograme2;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Android 18 on 4/22/2016.
 */
public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    public static final String TAG = EndlessScrollListener.class.getSimpleName();
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;
    private OnScrollToLastItemListener mOnScrollToLastItemListener;

    public EndlessScrollListener(RecyclerView recyclerView, OnScrollToLastItemListener onScrollToLastItemListener) {
        mRecyclerView = recyclerView;
        mOnScrollToLastItemListener = onScrollToLastItemListener;

        try {
            mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        } catch (ClassCastException e) {
            Log.e(TAG, "You should use LinearLayoutManager for the RecyclerView for this project.");
            e.printStackTrace();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = mRecyclerView.getChildCount();

        totalItemCount = mLayoutManager.getItemCount();
        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            Log.i("Yaeye!", "end called");

            //call the interface callback method.
            mOnScrollToLastItemListener.onScrollToLastItem();

            loading = true;
        }
    }

    public interface OnScrollToLastItemListener {
        public abstract void onScrollToLastItem();
    }
}
