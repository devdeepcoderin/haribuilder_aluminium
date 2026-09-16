package com.deepcoder.hbalum.utils;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.deepcoder.hbalum.BuildConfig;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;


public class PopUtils {

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

    public static void showDialogForCheckUpdate(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_check);

        setDialogInMeasuredScreenSize(dialog);

        AppCompatTextView tvUpdateNow = dialog.findViewById(R.id.tvUpdateNow);
        AppCompatTextView tvLater = dialog.findViewById(R.id.tvLater);
        AppCompatTextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);
        if (BuildConfig.DEBUG) {
            tvAppVersion.setText(context.getString(R.string.version).concat(BuildConfig.VERSION_NAME).concat(BuildConfig.BUILD_TYPE));
        } else {
            tvAppVersion.setText(context.getString(R.string.version).concat(BuildConfig.VERSION_NAME));
        }

        tvUpdateNow.setOnClickListener(view -> {
            StaticUtils.rateApp(context);
            dialog.cancel();
        });

        tvLater.setOnClickListener(v -> dialog.cancel());
        dialog.show();
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

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context, R.style.DialogTheme);
                }
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
                alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (changeButtonColor) {
                    try {
                        alert.setOnShowListener(arg0 -> alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (isOutSideCancelable) {
                    alert.setCanceledOnTouchOutside(true);
                } else {
                    alert.setCanceledOnTouchOutside(false);
                }
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle(title);
                builder.setItems(items, onItemClickListener);
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showRateAppDialog(Context context, final OnClickListener rateNowClickListener) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_rate_app);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AppCompatImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        AppCompatTextView tvLater = dialog.findViewById(R.id.tvLater);
        AppCompatTextView tvRateApp = dialog.findViewById(R.id.tvRateApp);
        LinearLayout llRateApp = dialog.findViewById(R.id.llRateApp);
        RelativeLayout rlMain = dialog.findViewById(R.id.rlMain);
        rlMain.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        tvLater.setVisibility(View.VISIBLE);
        tvRateApp.setVisibility(View.VISIBLE);

        tvLater.setOnClickListener(view -> {
            dialog.dismiss();
        });
        tvRateApp.setOnClickListener(view -> {
            StaticUtils.rateApp(context);
            dialog.dismiss();
        });
        ivCancel.setOnClickListener(v -> {
                    dialog.dismiss();
                }
        );
        dialog.show();
    }

    /**
     * This methods shows application update view.
     *
     * @param context                Instance on Activity
     * @param newAppVersion          Available application version
     * @param updateNowClickListener call back of update click listener.
     */
    public static void showDialogForUpdateApplication(Context context, String newAppVersion, final OnClickListener updateNowClickListener) {
        if(context == null)
            return;

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_app_update_available);

        setDialogInMeasuredScreenSize(dialog);

        TextView tvAppVersion = dialog.findViewById(R.id.tvAppVersion);

        TextView tvUpdateNow = dialog.findViewById(R.id.tvUpdateNow);
        TextView tvLater = dialog.findViewById(R.id.tvLater);

        tvAppVersion.setText(context.getResources().getString(R.string.new_version).concat(newAppVersion));

        tvUpdateNow.setOnClickListener(updateNowClickListener);

        tvLater.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }

    public static void showCustomViewWithButtons(Activity context, String title, String desc, String yesText, String noText, final OnClickListener onAllowClickListener) {
        showCustomViewWithButtons(context, title, desc, yesText, noText, onAllowClickListener, null);
    }

    public static void showCustomViewWithButtons(Activity context, String title, String desc, String yesText, final OnClickListener onAllowClickListener) {
        showCustomViewWithButtons(context, title, desc, yesText, "", onAllowClickListener, null);
    }

    private static void setDialogInMeasuredScreenSize(Dialog dialog) {

        WindowManager.LayoutParams windowManagerLayoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        if (window != null) {
            windowManagerLayoutParams.copyFrom(window.getAttributes());
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //This makes the dialog take up the full width
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

        if (noText.trim().isEmpty()) {
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


    public static void showCreateNewProjectDialog(Activity context, String title, boolean isEdit, ProjectData projectData,String PosBtnName,
                                                  StringsOnDialogButtonListener dialogButtonListener,
                                                  final OnClickListener onPositiveClickListener, final OnClickListener onNegativeClickListener) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_new_project);
        Integer newProjectNumberToSet = 0;
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
            edtProjectName.setText(String.valueOf(newProjectNumberToSet));
            edtProjectName.setText("Project".concat(" ").concat(String.valueOf(newProjectNumberToSet)));
        }

        Integer finalNewProjectNumberToSet = newProjectNumberToSet;
        btnPositive.setOnClickListener(v -> {

            if (TextUtils.isEmpty(Objects.requireNonNull(edtProjectName.getText()).toString())) {
                edtProjectName.setError(context.getString(R.string.please_enter_project_name));
                return;
            }
            DialogData dialogData = new DialogData();
            if (!isEdit) {
                dialogData.data1 = finalNewProjectNumberToSet.toString();
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

        edtWidth.setText("1200");
        edtHeight.setText("1500");
        edtQty.setText("1");

        edtWidth.setHint("1200");
        edtHeight.setHint("1500");
        edtQty.setHint("1");

        edtWidth.setSelection(edtWidth.getText().length());
        edtHeight.setSelection(edtHeight.getText().length());
        edtQty.setSelection(edtQty.getText().length());

        btnPositive.setOnClickListener(v -> {
            onPositiveClickListener.onClick(v);
            DialogData dialogData = new DialogData();
            if (TextUtils.isEmpty(edtWidth.getText().toString().trim())) {
                edtWidth.setText(edtWidth.getHint().toString());
            }
            if (TextUtils.isEmpty(edtHeight.getText().toString().trim())) {
                edtHeight.setText(edtHeight.getHint().toString());
            }
            if (TextUtils.isEmpty(edtQty.getText().toString().trim())) {
                edtQty.setText(edtQty.getHint().toString());
            }

            try {
                dialogData.data1 = edtWidth.getText().toString().trim();
                dialogData.data2 = edtHeight.getText().toString().trim();
                dialogData.data3 = edtQty.getText().toString().trim();
                dialogButtonListener.onButtonClick(dialogData);
            } catch (Exception e) {
                e.printStackTrace();
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


    public static BottomSheetDialog showOptionsWithItems(Activity context, int lstPosition, String title, ArrayList<BottomSheetData> lstData, OnStringItemClickListener clickListener) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        DialogListDataBinding dialogListDataBinding =DialogListDataBinding.inflate(context.getLayoutInflater());
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
        rvMoveCategoryList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL));
        mBottomSheetDialog.setContentView(dialogListDataBinding.getRoot());
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        return mBottomSheetDialog;
    }

    public static BottomSheetDialog showProjectDetail(Activity context, String title, ProjectData data) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        DialogProjectDetailBinding dialogProjectDetailBinding =DialogProjectDetailBinding.inflate(context.getLayoutInflater());


        dialogProjectDetailBinding.inheader.tvDialogTitle.setText(title);
        dialogProjectDetailBinding.inheader.ivClose.setVisibility(View.VISIBLE);


        ProjectManager projectManager = null;
        try {
            projectManager = ProjectManager.loadProjectFromPath(FileUtils.getProjectData(data.uuid).fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (projectManager == null) {
            mBottomSheetDialog.dismiss();
        }
        int valueOf = 0;
        float valueOf2 = 0.0f;
        float valueOf3 = 0.0f;
        float valueOf4 = 0.0f;
        Iterator<ProjectItemManager> it = projectManager.lstProjectItems.iterator();
        Integer num = valueOf;
        Float f = valueOf2;
        valueOf2 = valueOf3;
        valueOf3 = valueOf4;
        while (it.hasNext()) {
            ProjectItemManager designDataDetail = it.next();
            num = (num + designDataDetail.itemQuantity);
            f = f + (((float) designDataDetail.itemQuantity) * designDataDetail.m1294g());
            valueOf2 = valueOf2 + (((((float) designDataDetail.itemQuantity) * designDataDetail.m1291d()) * designDataDetail.m1290c()) / 1000000.0f);
            valueOf3 = (designDataDetail.m1283a(false) * ((float) designDataDetail.itemQuantity)) + valueOf3;
        }
        dialogProjectDetailBinding.tvQty.setText(String.valueOf(num));
        dialogProjectDetailBinding.tvProjectName.setText(projectManager.projectName);
        dialogProjectDetailBinding.tvTotalProfile.setText(String.format(Locale.US, "%.1f", f));
        dialogProjectDetailBinding.tvTotalItem.setText(String.format(Locale.US, "%.2f", valueOf2));
        dialogProjectDetailBinding.tvTotalGlassArea.setText(String.format(Locale.US, "%.1f", valueOf3));


        dialogProjectDetailBinding.inheader.ivClose.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        mBottomSheetDialog.setContentView(dialogProjectDetailBinding.getRoot());
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        return mBottomSheetDialog;
    }

    public static BottomSheetDialog showProjectItemDetail(Activity context, String title, ProjectItemManager data) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        
        DialogItemDetailBinding binding = DialogItemDetailBinding.inflate(context.getLayoutInflater());
        binding.inheader.tvDialogTitle.setText(title);
        binding.inheader.ivClose.setVisibility(View.VISIBLE);
        binding.inButtonItemDetail.llBottomMain.setVisibility(View.GONE);
        binding.tvItemNumber.setText(data.itemNumber.toString());
        binding.tvQuantity.setText(data.itemQuantity.toString());
        binding.tvItemName.setText(data.itemName);
        binding.tvDimension.setText(String.format(Locale.US, "%.0f", data.m1290c()).concat(" * ").concat(String.format(Locale.US, "%.0f", data.m1291d())));
        binding.tvProfileLength.setText(String.format(Locale.US, "%.1f", data.m1294g()));
        binding.tvItemArea.setText(String.format(Locale.US, "%.2f", (data.m1290c() * data.m1291d()) / 1000000.0f));
        binding.tvGlassArea.setText(String.format(Locale.US, "%.1f", data.m1283a(false)));


        binding.inheader.ivClose.setOnClickListener(v -> mBottomSheetDialog.dismiss());
        mBottomSheetDialog.setContentView(binding.getRoot());
        mBottomSheetDialog.show();
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        return mBottomSheetDialog;
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

        edtItemNumber.setText(projectItemManager.itemNumber.toString());
        edtQty.setText(projectItemManager.itemQuantity.toString());
        edtItemName.setText(projectItemManager.itemName);

        AppCompatButton btnNegative = dialog.findViewById(R.id.btnNegative);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnPositive);
        tvDialogTitle.setText(title);

        btnNegative.setText(context.getString(R.string.cancel));
        btnPositive.setText(context.getString(R.string.txt_add));

        edtQty.setSelection(edtQty.getText().length());

        btnPositive.setOnClickListener(v -> {
            onPositiveClickListener.onClick(v);
            DialogData dialogData = new DialogData();
            if (TextUtils.isEmpty(edtItemNumber.getText().toString().trim())) {
                edtItemNumber.setError(edtItemNumber.getHint().toString());
                return;
            }
            if (TextUtils.isEmpty(edtQty.getText().toString().trim())) {
                edtQty.setError(edtQty.getHint().toString());
                return;
            }

            try {
                dialogData.data1 = edtItemNumber.getText().toString().trim();
                dialogData.data2 = edtQty.getText().toString().trim();
                dialogData.data3 = edtItemName.getText().toString().trim();
                dialogButtonListener.onButtonClick(dialogData);
            } catch (Exception e) {
                e.printStackTrace();
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

        edtItemNumber.setText(String.valueOf(nextItemNumber));
        edtQty.setText(projectItemManager.itemQuantity.toString());

        AppCompatButton btnNegative = dialog.findViewById(R.id.btnNegative);
        AppCompatButton btnPositive = dialog.findViewById(R.id.btnPositive);
        tvDialogTitle.setText(title);

        btnNegative.setText(context.getString(R.string.cancel));
        btnPositive.setText(context.getString(R.string.create));

        edtQty.setSelection(edtQty.getText().length());

        btnPositive.setOnClickListener(v -> {
            onPositiveClickListener.onClick(v);
            DialogData dialogData = new DialogData();
            if (TextUtils.isEmpty(edtItemNumber.getText().toString().trim())) {
                edtItemNumber.setError(edtItemNumber.getHint().toString());
                return;
            }
            if (TextUtils.isEmpty(edtQty.getText().toString().trim())) {
                edtQty.setError(edtQty.getHint().toString());
                return;
            }

            try {
                dialogData.data1 = edtItemNumber.getText().toString().trim();
                dialogData.data2 = edtQty.getText().toString().trim();
                dialogButtonListener.onButtonClick(dialogData);
            } catch (Exception e) {
                e.printStackTrace();
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


}