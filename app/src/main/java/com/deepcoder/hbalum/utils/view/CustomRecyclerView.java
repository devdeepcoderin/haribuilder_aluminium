package com.deepcoder.hbalum.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.deepcoder.hbalum.R;


public class CustomRecyclerView extends RecyclerView {

    private Context context;
    private View emptyView;
    private View refreshLayoutView;
    private String emptyTitle;
    private String emptyDescription;
    private String emptyTextOnButton;
    private int imageId;
    AppCompatImageView btnEmpty;
    OnClickListener onClickListener;
    private boolean isShowLoader;

    /**
     * Constructor with 1 parameter context and attrs
     *
     * @param context context
     */
    public CustomRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Constructor with 2 parameters context and attrs
     *
     * @param context context
     * @param attrs   context
     */
    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCustomText(context, attrs);
    }

    /**
     * Initializes all the attributes and respective methods are called based on the attributes
     *
     * @param context context of activity
     * @param attrs   attrs
     */
    private void initCustomText(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView);

        int listOrientation = ta.getInt(R.styleable.CustomRecyclerView_list_orientation, 0);
        int listType = ta.getInt(R.styleable.CustomRecyclerView_list_type, 0);
        int grid_span = ta.getInt(R.styleable.CustomRecyclerView_gird_span, 3);

        /*
         * A custom getView uses isInEditMode() to determine whether or not it is being rendered inside the editor
         * and if so then loads test data instead of real data.
         */
        LayoutManager layoutManager;
        if (!isInEditMode()) {
            int layoutOrientation;
            switch (listOrientation) {
                case 0:
                    layoutOrientation = OrientationHelper.VERTICAL;
                    break;
                case 1:
                    layoutOrientation = OrientationHelper.HORIZONTAL;
                    break;
                default:
                    layoutOrientation = OrientationHelper.VERTICAL;
            }

            switch (listType) {
                case 0:
                    layoutManager = new LinearLayoutManager(context, layoutOrientation, false);
                    break;
                case 1:
                    layoutManager = new GridLayoutManager(context, grid_span, layoutOrientation, false);
                    break;
                case 2:
                    layoutManager = new StaggeredGridLayoutManager(grid_span, layoutOrientation);
                    break;
                default:
                    layoutManager = new LinearLayoutManager(context, layoutOrientation, false);
                    break;
            }

            setLayoutManager(layoutManager);

            ta.recycle();
        }
    }


    /**
     * setEmptyViewTextColor for change color of empty TextView
     *
     * @param color color code for apply in TextView
     */
    public void setEmptyViewTextColor(int color) {
        TextView tvEmptyTitle = emptyView.findViewById(R.id.tvEmptyTitle);
        tvEmptyTitle.setTextColor(ContextCompat.getColor(context, color));
    }

    // This observer  observes made changes in adapter
    private AdapterDataObserver observer = new AdapterDataObserver() {
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

    @SuppressWarnings("unused")
    public void setEmptyView(View emptyView, View refreshLayoutView) {
        this.refreshLayoutView = refreshLayoutView;
        setEmptyView(emptyView);
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
            if (refreshLayoutView != null) {
                refreshLayoutView.setVisibility(emptyViewVisible ? GONE : VISIBLE);
            }
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
            setDataInEmptyView();
        }
    }

    public void setEmptyData(String emptyTitle, String emptyDescription, String emptyTextOnButton, int imageId, boolean isShowLoader, OnClickListener onClickListener) {
        this.emptyTitle = emptyTitle;
        this.emptyDescription = emptyDescription;
        this.emptyTextOnButton = emptyTextOnButton;
        this.imageId = imageId;
        this.isShowLoader = isShowLoader;
        this.onClickListener = onClickListener;
        if (emptyView != null && emptyView.getVisibility() == VISIBLE) {
            setDataInEmptyView();
        }
    }

    @SuppressWarnings("unused")
    public void setEmptyData(String emptyTitle, int imageId, boolean isShowLoader, String emptyTextOnButton, OnClickListener onClickListener) {
        setEmptyData(emptyTitle, "", emptyTextOnButton, imageId, isShowLoader, onClickListener);
    }

    @SuppressWarnings("unused")
    public void setEmptyData(String emptyTitle, String emptyDescription, int imageId, boolean isShowLoader, OnClickListener onClickListener) {
        setEmptyData(emptyTitle, emptyDescription, "", imageId, isShowLoader, onClickListener);
    }

    @SuppressWarnings("unused")
    public void setEmptyData(String emptyTitle, int imageId, boolean isShowLoader) {
        setEmptyData(emptyTitle, "", "", imageId, isShowLoader, null);
    }

    @SuppressWarnings("unused")
    public void setEmptyData(String emptyTitle, String emptyDescription, boolean isShowLoader) {
        setEmptyData(emptyTitle, emptyDescription, "", 0, isShowLoader, null);
    }

    @SuppressWarnings("unused")
    public void setEmptyData(String emptyTitle, String emptyDescription, int imageId, boolean isShowLoader) {
        setEmptyData(emptyTitle, emptyDescription, "", imageId, isShowLoader, null);
    }

    @SuppressWarnings("unused")
    public void setEmptyData(String emptyTitle, boolean isShowLoader) {
        setEmptyData(emptyTitle, "", "", 0, isShowLoader, null);
    }

    public void setEmptyData(String emptyTitle) {
        setEmptyData(emptyTitle, "", "", 0, false, null);
    }

    public void setEmptyData(boolean isShowLoader) {
        setEmptyData("", "", "", 0, isShowLoader, null);
    }

    private void setDataInEmptyView() {

        TextView tvTitle = emptyView.findViewById(R.id.tvEmptyTitle);
        TextView tvDescription = emptyView.findViewById(R.id.tvEmptyDescription);
        ImageView ivImage = emptyView.findViewById(R.id.ivEmptyImage);
        btnEmpty = emptyView.findViewById(R.id.btnEmpty);
        ProgressBar pbLoader = emptyView.findViewById(R.id.pbLoader);


        Animation animationTitle = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        Animation animationDesc = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        Animation animationImage = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        Animation animationButton = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);

        tvDescription.setVisibility(TextUtils.isEmpty(emptyDescription) ? GONE : VISIBLE);
        tvTitle.setVisibility(TextUtils.isEmpty(emptyTitle) ? GONE : VISIBLE);
        btnEmpty.setVisibility(TextUtils.isEmpty(emptyTextOnButton) ? GONE : VISIBLE);
        ivImage.setVisibility(imageId <= 0 ? GONE : VISIBLE);
        pbLoader.setVisibility(isShowLoader ? VISIBLE : GONE);

        try {
            tvTitle.startAnimation(animationTitle);
            tvDescription.startAnimation(animationDesc);
            btnEmpty.startAnimation(animationButton);
            if (ivImage.getVisibility() == VISIBLE) {
                ivImage.setImageResource(imageId);
                ivImage.startAnimation(animationImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvTitle.setText(emptyTitle);
        tvDescription.setText(emptyDescription);
        //btnEmpty.setText(emptyTextOnButton);

        if (ivImage.getVisibility() == VISIBLE) {
            ivImage.setImageResource(imageId);
            ivImage.startAnimation(animationImage);
        }
        btnEmpty.setOnClickListener(v -> onClickListener.onClick(v));

    }
}
