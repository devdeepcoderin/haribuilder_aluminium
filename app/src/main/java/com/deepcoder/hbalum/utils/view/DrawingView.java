package com.deepcoder.hbalum.utils.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.activities.DesignActivity;
import com.deepcoder.hbalum.databinding.DialogChangeSizeBinding;
import com.deepcoder.hbalum.old.p006a.p008b.C0140d;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingPoints;
import com.deepcoder.hbalum.utils.StaticUtils;

import java.util.Locale;

public class DrawingView extends View {
    static C0140d c0140d;
    public static DesignActivity activity;

    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onDraw(Canvas canvas) {
        if (DesignActivity.checkActivityOrProjectNotNull()) {
            DesignActivity.drawingManager.f744b = DesignActivity.drawingManager.projectItemManager.drawData(canvas, new RectF(40.0f, 40.0f, ((float) getWidth()) - 40.0f, (((float) getHeight()) - 40.0f) - 0.0f), true);
        }
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            DrawingPoints drawingPoints = DesignActivity.drawingManager.f744b.setDrawingPoints(new DrawingPoints(motionEvent.getX(), motionEvent.getY()));
            C0140d c0140dLocal = null;
            for (Object object : DesignActivity.drawingManager.projectItemManager.f779f) {
                C0140d c0140d2 = (C0140d) object;
                if (StaticUtils.m1365a(drawingPoints, c0140d2.f763n)) {
                    if (c0140dLocal == null) {
                        c0140dLocal = c0140d2;
                    } else {
                        if (StaticUtils.m1367b(StaticUtils.m1354a(new DrawingPoints(c0140d2.f763n.left, c0140d2.f763n.top), new DrawingPoints(c0140d2.f763n.right, c0140d2.f763n.bottom), 0.5f), drawingPoints) < StaticUtils.m1367b(StaticUtils.m1354a(new DrawingPoints(c0140dLocal.f763n.left, c0140dLocal.f763n.top), new DrawingPoints(c0140dLocal.f763n.right, c0140dLocal.f763n.bottom), 0.5f), drawingPoints)) {
                            c0140dLocal = c0140d2;
                        }
                    }
                }
            }
            if (c0140dLocal != null) {
                c0140d = c0140dLocal;

                DialogChangeSizeBinding binding = DialogChangeSizeBinding.inflate(LayoutInflater.from(getContext()));
                Builder builder = new Builder(getContext(), 3);
                builder.setView(binding.getRoot());

                AlertDialog alertDialog = builder.create();

                binding.inheader.tvDialogTitle.setText(getContext().getString(R.string.set_view_size));
                binding.edtSize.setText(String.format(Locale.ENGLISH, "%.0f", c0140d.f752c));
                binding.inBottomButtons.btnPositive.setText(getContext().getString(R.string.ok));
                binding.inBottomButtons.btnNegative.setText(getContext().getString(R.string.cancel));

                binding.edtSize.setSelection(binding.edtSize.getText().length());

                binding.inBottomButtons.btnNegative.setOnClickListener(v -> alertDialog.dismiss());

                binding.inBottomButtons.btnPositive.setOnClickListener(v -> {
                    if (binding.edtSize.getText().toString().trim().isEmpty())
                        return;

                    alertDialog.dismiss();
                    DesignActivity.drawingManager.setDimensionOnEdit(DrawingView.c0140d, (float) Integer.parseInt(binding.edtSize.getText().toString().trim()));
                });

                alertDialog.setOnDismissListener(dialog -> closeKeyboard());
                alertDialog.show();

                showKeyboard();

                return true;
            }
        }
        return false;
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
