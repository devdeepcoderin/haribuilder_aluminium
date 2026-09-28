package com.deepcoder.hbalum.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.deepcoder.hbalum.R;


public class CustomRecyclerView extends RecyclerView {

    private static final String TAG = CustomRecyclerView.class.getSimpleName();

    private View emptyView;
    private String emptyTitle;

    /**
     * Constructor with 1 parameter context and attrs
     *
     * @param context context
     */
    public CustomRecyclerView(Context context) {
        super(context);
    }

    /**
     * Constructor with 2 parameters context and attrs
     *
     * @param context context
     * @param attrs   context
     */
    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomText(context, attrs);
    }

    /**
     * Initializes all the attributes and respective methods are called based on the attributes
     *
     * @param context context of activity
     * @param attrs   attrs
     */
    private void initCustomText(Context context, AttributeSet attrs) {
        try (TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView)) {
            int listOrientation = ta.getInt(R.styleable.CustomRecyclerView_list_orientation, 0);
            int listType = ta.getInt(R.styleable.CustomRecyclerView_list_type, 0);
            int gridSpan = ta.getInt(R.styleable.CustomRecyclerView_gird_span, 3);

            /*
             * A custom getView uses isInEditMode() to determine whether or not it is being rendered inside the editor
             * and if so then loads test data instead of real data.
             */
            if (!isInEditMode()) {
                int layoutOrientation = listOrientation == 1 ? RecyclerView.HORIZONTAL : RecyclerView.VERTICAL;

                LayoutManager layoutManager = switch (listType) {
                    case 1 -> new GridLayoutManager(context, gridSpan, layoutOrientation, false);
                    case 2 -> new StaggeredGridLayoutManager(gridSpan, layoutOrientation);
                    default -> new LinearLayoutManager(context, layoutOrientation, false);
                };

                setLayoutManager(layoutManager);
            }
        }
    }


    // This observer  observes made changes in adapter
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    /**
     * This function checks data empty in recycler-view.
     */
    void checkIfEmpty() {

        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
            setDataInEmptyView();
        }
    }

    public void setEmptyData(String emptyTitle) {
        this.emptyTitle = emptyTitle;
        if (emptyView != null && emptyView.getVisibility() == VISIBLE) {
            setDataInEmptyView();
        }
    }

    private void setDataInEmptyView() {

        TextView tvTitle = emptyView.findViewById(R.id.tvEmptyTitle);
        TextView tvDescription = emptyView.findViewById(R.id.tvEmptyDescription);


        Animation animationTitle = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);

        tvDescription.setVisibility(GONE);
        tvTitle.setVisibility(TextUtils.isEmpty(emptyTitle) ? GONE : VISIBLE);

        try {
            tvTitle.startAnimation(animationTitle);
        } catch (Exception e) {
            Log.e(TAG, "Unable to animate empty view", e);
        }

        tvTitle.setText(emptyTitle);

    }
}
