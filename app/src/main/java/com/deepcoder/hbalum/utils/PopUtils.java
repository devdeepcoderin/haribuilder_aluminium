package com.deepcoder.hbalum.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.adapter.DialogDataAdapter;
import com.deepcoder.hbalum.databinding.DialogCustomLayoutBinding;
import com.deepcoder.hbalum.databinding.DialogItemDetailBinding;
import com.deepcoder.hbalum.databinding.DialogListDataBinding;
import com.deepcoder.hbalum.databinding.DialogProjectDetailBinding;
import com.deepcoder.hbalum.datalayers.model.BottomSheetData;
import com.deepcoder.hbalum.datalayers.model.DialogData;
import com.deepcoder.hbalum.datalayers.model.ProjectData;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.manager.ProjectManager;
import com.deepcoder.hbalum.myinterface.OnStringItemClickListener;
import com.deepcoder.hbalum.myinterface.StringsOnDialogButtonListener;
import com.deepcoder.hbalum.utils.view.CustomRecyclerView;
import com.deepcoder.hbalum.utils.logger.CustomLog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class PopUtils {

    private static final String TAG = "PopUtils";

    /**
     * Method to show alert dialog with two/single button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param nagButtonName         Name of negative button
     * @param onPositiveButtonClick call back method of positive button
     * @param onNegativeButtonClick call back method of negative butoon
     */
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      String nagButtonName, boolean isOutSideCancelable,
                                                      DialogInterface.OnClickListener onPositiveButtonClick,
                                                      DialogInterface.OnClickListener onNegativeButtonClick) {
        showCustomTwoButtonAlertDialog(context, title, message, posButtonName, nagButtonName, false, isOutSideCancelable, onPositiveButtonClick, onNegativeButtonClick);

    }

    /**
     * Method to show alert dialog with single  positive  button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param onPositiveButtonClick call back method of positive button
     */
    @SuppressWarnings("unused")
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      boolean isOutSideCancelable, DialogInterface.OnClickListener onPositiveButtonClick) {
        showCustomTwoButtonAlertDialog(context, title, message, posButtonName, "", false, isOutSideCancelable, onPositiveButtonClick, null);

    }

    /**
     * Method to show alert dialog with two/single button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param nagButtonName         Name of negative button
     * @param onPositiveButtonClick call back method of positive button
     * @param onNegativeButtonClick call back method of negative butoon
     */
    @SuppressWarnings("unused")
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      String nagButtonName, boolean changeButtonColor, boolean isOutSideCancelable,
                                                      DialogInterface.OnClickListener onPositiveButtonClick,
                                                      DialogInterface.OnClickListener onNegativeButtonClick) {
        try {
            if (context != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog);
                builder.setMessage(message);
                if (TextUtils.isEmpty(title)) {
                    builder.setTitle(context.getResources().getString(R.string.app_name));
                } else {
                    builder.setTitle(title);
                }

                if (onPositiveButtonClick != null) {
                    builder.setPositiveButton(posButtonName, onPositiveButtonClick);
                }
                if (onNegativeButtonClick != null) {
                    builder.setNegativeButton(nagButtonName, onNegativeButtonClick);
                }

                builder.setCancelable(true);
                final AlertDialog alert = builder.create();
                if (changeButtonColor) {
                    try {
                        alert.setOnShowListener(arg0 -> alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED));
                    } catch (Exception e) {
                        CustomLog.errorException(TAG, e);
                    }
                }
                alert.setCanceledOnTouchOutside(isOutSideCancelable);
                alert.show();
            }
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    /**
     * Method to show alertDialog with items array
     *
     * @param context             context
     * @param items               Array of data which are display in dialog
     * @param title               title of the alert dialog
     * @param onItemClickListener call back method of selected item
     */
    @SuppressWarnings("unused")
    public static void showCustomAlertDialogWithItems(Context context, CharSequence[] items,
                                                      final String title,
                                                      DialogInterface.OnClickListener onItemClickListener) {
        try {
            if (context != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_App_MaterialAlertDialog);
                builder.setTitle(title);
                builder.setItems(items, onItemClickListener);
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
        }
    }

    public static void showCustomViewWithButtons(Activity context, String title, String desc, String yesText, String noText, final OnClickListener onAllowClickListener) {
        showCustomViewWithButtons(context, title, desc, yesText, noText, onAllowClickListener, null);
    }

    private static void setDialogInMeasuredScreenSize(Dialog dialog) {
        WindowManager.LayoutParams windowManagerLayoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        if (window != null) {
            windowManagerLayoutParams.copyFrom(window.getAttributes());
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            windowManagerLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            windowManagerLayoutParams.width = StaticData.SCREEN_WIDTH - ((StaticData.SCREEN_WIDTH) / 20);
            window.setAttributes(windowManagerLayoutParams);
        }
    }

    public static void showCustomViewWithButtons(Activity context, String title, String desc, String yesText, String noText,
                                                 final OnClickListener onAllowClickListener, final OnClickListener onDenyClickListener) {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        DialogCustomLayoutBinding layoutBinding = DialogCustomLayoutBinding.inflate(context.getLayoutInflater());
        layoutBinding.inheader.tvDialogTitle.setText(title);
        layoutBinding.tvMessage.setText(desc);
        layoutBinding.btnPositive.setText(yesText);

        layoutBinding.inheader.ivClose.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        mBottomSheetDialog.setContentView(layoutBinding.getRoot());

        layoutBinding.btnPositive.setOnClickListener(v -> {
            onAllowClickListener.onClick(v);
            mBottomSheetDialog.dismiss();
        });

        if (TextUtils.isEmpty(noText) || noText.trim().isEmpty()) {
            layoutBinding.btnNegative.setVisibility(View.GONE);
        } else {
            layoutBinding.btnNegative.setText(noText);
        }

        if (onDenyClickListener != null) {
            layoutBinding.btnNegative.setOnClickListener(v -> {
                onDenyClickListener.onClick(v);
                mBottomSheetDialog.dismiss();
            });
        } else {
            layoutBinding.btnNegative.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        }
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
    }


    public static void showCreateNewProjectDialog(Activity context, String title, boolean isEdit, ProjectData projectData, String PosBtnName,
                                                  StringsOnDialogButtonListener dialogButtonListener,
                                                  final OnClickListener onPositiveClickListener, final OnClickListener onNegativeClickListener) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_new_project);
        int newProjectNumberToSet = 0;
        setDialogInMeasuredScreenSize(dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        AppCompatButton btnNegative = dialog.findViewById(R.id.btnNegative);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnPositive);
        AppCompatEditText edtProjectName = dialog.findViewById(R.id.edtProjectName);
        tvDialogTitle.setText(title);
        btnNegative.setText(context.getString(R.string.cancel));
        btnPositive.setText(PosBtnName);

        if (isEdit) {
            edtProjectName.setText(projectData.projectName);
        } else {
            newProjectNumberToSet = FileUtils.getNewProjectNumber();
            edtProjectName.setText(context.getString(R.string.project_name_with_number, newProjectNumberToSet));
        }

        int finalNewProjectNumberToSet = newProjectNumberToSet;
        btnPositive.setOnClickListener(v -> {

            if (TextUtils.isEmpty(Objects.requireNonNull(edtProjectName.getText()).toString())) {
                edtProjectName.setError(context.getString(R.string.please_enter_project_name));
                return;
            }
            DialogData dialogData = new DialogData();
            if (!isEdit) {
                dialogData.data1 = String.format(Locale.getDefault(), "%d", finalNewProjectNumberToSet);
            }
            dialogData.data2 = edtProjectName.getText().toString().trim();
            dialogButtonListener.onButtonClick(dialogData);
            onPositiveClickListener.onClick(v);
            dialog.dismiss();
        });

        if (onNegativeClickListener != null) {
            btnNegative.setOnClickListener(v -> {
                onNegativeClickListener.onClick(v);
                dialog.dismiss();
            });
        } else {
            btnNegative.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    public static void showCreateNewProjectItemDialog(Activity context, String title, String btnPosName,
                                                      StringsOnDialogButtonListener dialogButtonListener, final OnClickListener onPositiveClickListener, final OnClickListener onNegativeClickListener) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_new_project_item);

        setDialogInMeasuredScreenSize(dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        AppCompatButton btnNegative = dialog.findViewById(R.id.btnNegative);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnPositive);
        AppCompatEditText edtWidth = dialog.findViewById(R.id.edtWidth);
        AppCompatEditText edtHeight = dialog.findViewById(R.id.edtHeight);
        AppCompatEditText edtQty = dialog.findViewById(R.id.edtQty);
        tvDialogTitle.setText(title);

        btnNegative.setText(context.getString(R.string.cancel));
        btnPositive.setText(btnPosName);

        String defaultWidth = context.getString(R.string.default_item_width);
        String defaultHeight = context.getString(R.string.default_item_height);
        String defaultQty = context.getString(R.string.default_item_qty);

        edtWidth.setText(defaultWidth);
        edtHeight.setText(defaultHeight);
        edtQty.setText(defaultQty);

        edtWidth.setHint(defaultWidth);
        edtHeight.setHint(defaultHeight);
        edtQty.setHint(defaultQty);

        setSelectionAtEnd(edtWidth);
        setSelectionAtEnd(edtHeight);
        setSelectionAtEnd(edtQty);

        btnPositive.setOnClickListener(v -> {
            onPositiveClickListener.onClick(v);
            DialogData dialogData = new DialogData();
            if (TextUtils.isEmpty(safeText(edtWidth))) {
                edtWidth.setText(edtWidth.getHint());
            }
            if (TextUtils.isEmpty(safeText(edtHeight))) {
                edtHeight.setText(edtHeight.getHint());
            }
            if (TextUtils.isEmpty(safeText(edtQty))) {
                edtQty.setText(edtQty.getHint());
            }

            try {
                dialogData.data1 = safeText(edtWidth).trim();
                dialogData.data2 = safeText(edtHeight).trim();
                dialogData.data3 = safeText(edtQty).trim();
                dialogButtonListener.onButtonClick(dialogData);
            } catch (Exception e) {
                CustomLog.errorException(TAG, e);
            }
            dialog.dismiss();
        });

        if (onNegativeClickListener != null) {
            btnNegative.setOnClickListener(v -> {
                onNegativeClickListener.onClick(v);
                dialog.dismiss();
            });
        } else {
            btnNegative.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }


    public static void showOptionsWithItems(Activity context, int lstPosition, String title, ArrayList<BottomSheetData> lstData, OnStringItemClickListener clickListener) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        DialogListDataBinding dialogListDataBinding = DialogListDataBinding.inflate(context.getLayoutInflater());
        CustomRecyclerView rvMoveCategoryList = dialogListDataBinding.rvData;
        dialogListDataBinding.inheader.tvDialogTitle.setText(title);
        dialogListDataBinding.inheader.ivClose.setVisibility(View.VISIBLE);

        DialogDataAdapter advertiseAdapter = new DialogDataAdapter(context, lstData) {

            @Override
            public void onItemClick(int position, String item) {
                clickListener.onStringItemClick(lstPosition, position, item);
                mBottomSheetDialog.dismiss();
            }
        };

        dialogListDataBinding.inheader.ivClose.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        rvMoveCategoryList.setAdapter(advertiseAdapter);
        mBottomSheetDialog.setContentView(dialogListDataBinding.getRoot());
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
    }

    public static void showProjectDetail(Activity context, String title, ProjectData data) {
        ProjectData projectFile = FileUtils.getProjectData(data.uuid);
        if (projectFile == null) {
            return;
        }

        ProjectManager projectManager;
        try {
            projectManager = ProjectManager.loadProjectFromPath(projectFile.fileName);
        } catch (Exception e) {
            CustomLog.errorException(TAG, e);
            return;
        }

        if (projectManager.lstProjectItems == null) {
            return;
        }

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        DialogProjectDetailBinding dialogProjectDetailBinding = DialogProjectDetailBinding.inflate(context.getLayoutInflater());

        dialogProjectDetailBinding.inheader.tvDialogTitle.setText(title);
        dialogProjectDetailBinding.inheader.ivClose.setVisibility(View.VISIBLE);

        int totalQty = 0;
        float totalProfile = 0f;
        float totalItemArea = 0f;
        float totalGlassArea = 0f;
        for (ProjectItemManager designDataDetail : projectManager.lstProjectItems) {
            int qty = designDataDetail.itemQuantity != null ? designDataDetail.itemQuantity : 0;
            totalQty += qty;
            totalProfile += ((float) qty) * designDataDetail.m1294g();
            totalItemArea += ((((float) qty) * designDataDetail.m1291d()) * designDataDetail.m1290c()) / 1000000.0f;
            totalGlassArea += designDataDetail.m1283a(false) * ((float) qty);
        }
        dialogProjectDetailBinding.tvQty.setText(String.format(Locale.getDefault(), "%d", totalQty));
        dialogProjectDetailBinding.tvProjectName.setText(projectManager.projectName);
        dialogProjectDetailBinding.tvTotalProfile.setText(String.format(Locale.getDefault(), "%.1f", totalProfile));
        dialogProjectDetailBinding.tvTotalItem.setText(String.format(Locale.getDefault(), "%.2f", totalItemArea));
        dialogProjectDetailBinding.tvTotalGlassArea.setText(String.format(Locale.getDefault(), "%.1f", totalGlassArea));

        dialogProjectDetailBinding.inheader.ivClose.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        mBottomSheetDialog.setContentView(dialogProjectDetailBinding.getRoot());
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
    }

    public static void showProjectItemDetail(Activity context, String title, ProjectItemManager data) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

        DialogItemDetailBinding binding = DialogItemDetailBinding.inflate(context.getLayoutInflater());
        binding.inheader.tvDialogTitle.setText(title);
        binding.inheader.ivClose.setVisibility(View.VISIBLE);
        binding.inButtonItemDetail.llBottomMain.setVisibility(View.GONE);
        binding.tvItemNumber.setText(formatInteger(data.itemNumber));
        binding.tvQuantity.setText(formatInteger(data.itemQuantity));
        binding.tvItemName.setText(data.itemName);
        binding.tvDimension.setText(String.format(Locale.getDefault(), "%.0f * %.0f", data.m1290c(), data.m1291d()));
        binding.tvProfileLength.setText(String.format(Locale.getDefault(), "%.1f", data.m1294g()));
        binding.tvItemArea.setText(String.format(Locale.getDefault(), "%.2f", (data.m1290c() * data.m1291d()) / 1000000.0f));
        binding.tvGlassArea.setText(String.format(Locale.getDefault(), "%.1f", data.m1283a(false)));

        binding.inheader.ivClose.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        mBottomSheetDialog.setContentView(binding.getRoot());
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
    }

    public static void editProjectItem(Activity context, String title, ProjectItemManager projectItemManager, StringsOnDialogButtonListener dialogButtonListener, final OnClickListener onPositiveClickListener, final OnClickListener onNegativeClickListener) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_edit_item);
        setDialogInMeasuredScreenSize(dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        TextInputEditText edtItemNumber = dialog.findViewById(R.id.edtItemNumber);
        TextInputEditText edtQty = dialog.findViewById(R.id.edtQty);
        TextInputEditText edtItemName = dialog.findViewById(R.id.edtItemName);

        edtItemNumber.setText(formatInteger(projectItemManager.itemNumber));
        edtQty.setText(formatInteger(projectItemManager.itemQuantity));
        edtItemName.setText(projectItemManager.itemName);

        AppCompatButton btnNegative = dialog.findViewById(R.id.btnNegative);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnPositive);
        tvDialogTitle.setText(title);

        btnNegative.setText(context.getString(R.string.cancel));
        btnPositive.setText(context.getString(R.string.txt_add));

        setSelectionAtEnd(edtQty);

        btnPositive.setOnClickListener(v -> {
            onPositiveClickListener.onClick(v);
            DialogData dialogData = new DialogData();
            if (TextUtils.isEmpty(safeText(edtItemNumber).trim())) {
                CharSequence hint = edtItemNumber.getHint();
                if (hint != null) {
                    edtItemNumber.setError(hint.toString());
                }
                return;
            }
            if (TextUtils.isEmpty(safeText(edtQty).trim())) {
                CharSequence hint = edtQty.getHint();
                if (hint != null) {
                    edtQty.setError(hint.toString());
                }
                return;
            }

            try {
                dialogData.data1 = safeText(edtItemNumber).trim();
                dialogData.data2 = safeText(edtQty).trim();
                dialogData.data3 = safeText(edtItemName).trim();
                dialogButtonListener.onButtonClick(dialogData);
            } catch (Exception e) {
                CustomLog.errorException(TAG, e);
            }
            dialog.dismiss();
        });

        if (onNegativeClickListener != null) {
            btnNegative.setOnClickListener(v -> {
                onNegativeClickListener.onClick(v);
                dialog.dismiss();
            });
        } else {
            btnNegative.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }


    public static void copyProjectItem(Activity context, String title, ProjectItemManager projectItemManager, int nextItemNumber, StringsOnDialogButtonListener dialogButtonListener, final OnClickListener onPositiveClickListener, final OnClickListener onNegativeClickListener) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_copy_item);
        setDialogInMeasuredScreenSize(dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);
        TextInputEditText edtItemNumber = dialog.findViewById(R.id.edtItemNumber);
        TextInputEditText edtQty = dialog.findViewById(R.id.edtQty);

        edtItemNumber.setText(String.format(Locale.getDefault(), "%d", nextItemNumber));
        edtQty.setText(formatInteger(projectItemManager.itemQuantity));

        AppCompatButton btnNegative = dialog.findViewById(R.id.btnNegative);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnPositive);
        tvDialogTitle.setText(title);

        btnNegative.setText(context.getString(R.string.cancel));
        btnPositive.setText(context.getString(R.string.create));

        setSelectionAtEnd(edtQty);

        btnPositive.setOnClickListener(v -> {
            onPositiveClickListener.onClick(v);
            DialogData dialogData = new DialogData();
            if (TextUtils.isEmpty(safeText(edtItemNumber).trim())) {
                CharSequence hint = edtItemNumber.getHint();
                if (hint != null) {
                    edtItemNumber.setError(hint.toString());
                }
                return;
            }
            if (TextUtils.isEmpty(safeText(edtQty).trim())) {
                CharSequence hint = edtQty.getHint();
                if (hint != null) {
                    edtQty.setError(hint.toString());
                }
                return;
            }

            try {
                dialogData.data1 = safeText(edtItemNumber).trim();
                dialogData.data2 = safeText(edtQty).trim();
                dialogButtonListener.onButtonClick(dialogData);
            } catch (Exception e) {
                CustomLog.errorException(TAG, e);
            }
            dialog.dismiss();
        });

        if (onNegativeClickListener != null) {
            btnNegative.setOnClickListener(v -> {
                onNegativeClickListener.onClick(v);
                dialog.dismiss();
            });
        } else {
            btnNegative.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    private static String formatInteger(Integer value) {
        return value == null ? "" : String.format(Locale.getDefault(), "%d", value);
    }

    private static String safeText(TextView editText) {
        CharSequence text = editText.getText();
        return text == null ? "" : text.toString();
    }

    private static void setSelectionAtEnd(EditText editText) {
        CharSequence text = editText.getText();
        if (text != null) {
            editText.setSelection(text.length());
        }
    }
}
