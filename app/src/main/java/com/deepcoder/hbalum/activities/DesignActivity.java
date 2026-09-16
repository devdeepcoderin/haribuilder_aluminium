package com.deepcoder.hbalum.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.deepcoder.hbalum.R;
import com.deepcoder.hbalum.application.BaseApplication;
import com.deepcoder.hbalum.databinding.ActivityDesignBinding;
import com.deepcoder.hbalum.datalayers.model.BottomSheetData;
import com.deepcoder.hbalum.enums.DirectionsEnum;
import com.deepcoder.hbalum.enums.DrawOrientationEnum;
import com.deepcoder.hbalum.enums.DrawTypeEnum;
import com.deepcoder.hbalum.enums.WingFunctionTypeEnum;
import com.deepcoder.hbalum.manager.ProjectItemManager;
import com.deepcoder.hbalum.manager.ProjectManager;
import com.deepcoder.hbalum.manager.ProjectSubItemManager;
import com.deepcoder.hbalum.old.p006a.p008b.C0142t;
import com.deepcoder.hbalum.old.p006a.p008b.C0156s;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingManager;
import com.deepcoder.hbalum.old.p006a.p008b.DrawingPoints;
import com.deepcoder.hbalum.old.p006a.p008b.GlazingBead;
import com.deepcoder.hbalum.old.p006a.p008b.WingFunction;
import com.deepcoder.hbalum.utils.PopUtils;
import com.deepcoder.hbalum.utils.SimpleItemGestureTouch;
import com.deepcoder.hbalum.utils.StaticUtils;
import com.deepcoder.hbalum.utils.view.DrawingView;

import java.util.ArrayList;


public class DesignActivity extends BaseActivity implements View.OnTouchListener, View.OnDragListener {
    public static DrawingManager drawingManager;
    public static ProjectManager projectManager;
    public static DrawingView drawingView;

    private ActivityDesignBinding binding;

    private int position;

    public static boolean checkActivityOrProjectNotNull() {
        return drawingManager != null && drawingManager.projectItemManager != null;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityDesignBinding.inflate(getLayoutInflater());
        StaticUtils.fullScreenModeManage(getWindow(),binding.getRoot());
        setContentView(binding.getRoot());
        getBundleData();
        init();
        DrawingView.activity = this;
    }

    private void getBundleData() {
        position = getIntent().getIntExtra("position", -1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        projectManager.generateXMLFile(false,-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        m1181g();
    }

    public void copyItem(ProjectItemManager projectItemManager) {
        PopUtils.copyProjectItem(this, getString(R.string.copy_item), projectItemManager, DesignActivity.projectManager.getProjectItemNumber(), dialogData -> {
            copyItemData(copyProjectItem(dialogData, projectItemManager.drawData()));
        }, view -> {
        }, null);

    }

    public void editItem(ProjectItemManager projectItemManager) {
        PopUtils.editProjectItem(this, getString(R.string.add_qty), projectItemManager, dialogData -> {
            editProjectItemData(dialogData, projectItemManager);
            updateItemNumber();
        }, view -> {
        }, null);
    }

    private int m1173a(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getLeft();
        }
        return m1173a((View) view.getParent()) + view.getLeft();
    }

    private int m1177b(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        }
        return m1177b((View) view.getParent()) + view.getTop();
    }

    private void m1181g() {
        try {
            if (position >= 0) {
                drawingManager = new DrawingManager();
                drawingManager.projectItemManager = projectManager.lstProjectItems.get(position);
                drawingManager.projectItemManager.refresh();
                drawingManager.addDataInUndoList(true);
            }

            binding.dotsIndicator.setDotsCount(projectManager.lstProjectItems.size());

            if (position >= 0) {
                binding.dotsIndicator.setCurrentSelectedDot(position);
            } else {
                binding.dotsIndicator.setCurrentSelectedDot(projectManager.lstProjectItems.size() - 1);
            }

            updateItemNumber();
        } catch (Exception e) {
            if (!(e.getMessage() == null)) {
                e.printStackTrace();
                if (BaseApplication.isShowExceptionToast) {
                    showToastForLongTime(e.getMessage());
                }
            }
            finish();
        }
    }

    public void updateItemNumber() {
        binding.inheader.tvItemNumber.setText(drawingManager.projectItemManager.itemNumber.toString());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        drawingView = binding.drawing;

        // Set Draw Types Tag To ImageViews
        binding.ivVerticalMullion.setTag(DrawTypeEnum.MullionVerticalSingle.name());
        binding.ivHorizontalMullion.setTag(DrawTypeEnum.MullionHorizontal.name());
        binding.ivTilt.setTag(DrawTypeEnum.Tilt.name());
        binding.ivTurnRight.setTag(DrawTypeEnum.TurnRight.name());
        binding.ivTurnLeft.setTag(DrawTypeEnum.TurnLeft.name());
        binding.ivDoorLeft.setTag(DrawTypeEnum.DoorLeft.name());
        binding.ivDoorRight.setTag(DrawTypeEnum.DoorRight.name());
        binding.ivDoorOpen.setTag(DrawTypeEnum.DoorOpen.name());
        binding.ivHorizontalPanel.setTag(DrawTypeEnum.PanelHorizontal.name());
        binding.ivVerticalPanel.setTag(DrawTypeEnum.PanelVertical.name());
        binding.ivSlidingRight.setTag(DrawTypeEnum.SlidingRight.name());
        binding.ivSlidingLeft.setTag(DrawTypeEnum.SlidingLeft.name());
        binding.ivSliding2A.setTag(DrawTypeEnum.Sliding2A.name());
        binding.ivSliding2C.setTag(DrawTypeEnum.Sliding2C.name());
        binding.ivSliding3F.setTag(DrawTypeEnum.Sliding3F.name());
        binding.ivSliding3H.setTag(DrawTypeEnum.Sliding3H.name());
        binding.ivSliding4A.setTag(DrawTypeEnum.Sliding4A.name());
        binding.ivSliding2B.setTag(DrawTypeEnum.Sliding2B.name());
        binding.ivSliding3A.setTag(DrawTypeEnum.Sliding3A.name());
        binding.ivSliding3G.setTag(DrawTypeEnum.Sliding3G.name());
        binding.ivSliding3I.setTag(DrawTypeEnum.Sliding3I.name());
        binding.ivSliding4B.setTag(DrawTypeEnum.Sliding4B.name());

        // Set Touch Listeners for Draw Types ImageViews
        binding.ivVerticalMullion.setOnTouchListener(this);
        binding.ivHorizontalMullion.setOnTouchListener(this);
        binding.ivTilt.setOnTouchListener(this);
        binding.ivTurnRight.setOnTouchListener(this);
        binding.ivTurnLeft.setOnTouchListener(this);
        binding.ivDoorLeft.setOnTouchListener(this);
        binding.ivDoorRight.setOnTouchListener(this);
        binding.ivDoorOpen.setOnTouchListener(this);
        binding.ivHorizontalPanel.setOnTouchListener(this);
        binding.ivVerticalPanel.setOnTouchListener(this);
        binding.ivSlidingLeft.setOnTouchListener(this);
        binding.ivSlidingRight.setOnTouchListener(this);

        binding.ivSliding2A.setOnTouchListener(this);
        binding.ivSliding2C.setOnTouchListener(this);
        binding.ivSliding3F.setOnTouchListener(this);
        binding.ivSliding3H.setOnTouchListener(this);
        binding.ivSliding4A.setOnTouchListener(this);
        binding.ivSliding2B.setOnTouchListener(this);
        binding.ivSliding3A.setOnTouchListener(this);
        binding.ivSliding3G.setOnTouchListener(this);
        binding.ivSliding3I.setOnTouchListener(this);
        binding.ivSliding4B.setOnTouchListener(this);

        // Set Drag Listener
        drawingView.setOnDragListener(this);

        binding.llMain.setOnTouchListener(new SimpleItemGestureTouch(this, BaseApplication.getInstance()));

        binding.dotsIndicator.setDotClickListener(this::moveToSpecificPosition);
        binding.inheader.imgBtnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.inheader.ivUndo.setOnClickListener(v -> drawingManager.undo());
        binding.inheader.ivRedo.setOnClickListener(v -> drawingManager.redo());
        binding.inheader.ivCreate.setOnClickListener(v -> showCreateNewProjectDialog());
        binding.inheader.ivOptions.setOnClickListener(v -> showMoreOptions());
    }

    private void moveToSpecificPosition(int pos) {
        this.position = pos;
        m1181g();
    }

    protected void copyItemData(ProjectItemManager itemManager) {
        position = projectManager.lstProjectItems.indexOf(itemManager);
        m1181g();
    }

    protected void deleteItemDialog() {
        if (checkActivityOrProjectNotNull()) {
            PopUtils.showCustomViewWithButtons(this, getString(R.string.delete_item),
                    getString(R.string.current_item_delete_conform_msg), getString(R.string.yes), getString(R.string.no), view -> {
                        ProjectItemManager designDataDetail = drawingManager.projectItemManager;

                        if (projectManager.lstProjectItems.size() != 1) {
                            if (projectManager.lstProjectItems.indexOf(designDataDetail) < projectManager.lstProjectItems.size() - 1) {
                                nextItem();
                            } else {
                                previousItem();
                            }
                        }

                        projectManager.lstProjectItems.remove(designDataDetail);

                        binding.dotsIndicator.setDotsCount(projectManager.lstProjectItems.size());

                        if (projectManager.lstProjectItems.isEmpty()) {
                            finish();
                        }
                    }
            );
        }
    }

    public void previousItem() {
        if (checkActivityOrProjectNotNull() && projectManager.lstProjectItems.indexOf(drawingManager.projectItemManager) > 0) {
            position = projectManager.lstProjectItems.indexOf(drawingManager.projectItemManager) - 1;
            m1181g();

        }
    }

    public void nextItem() {
        if (checkActivityOrProjectNotNull() && projectManager.lstProjectItems.indexOf(drawingManager.projectItemManager) < projectManager.lstProjectItems.size() - 1) {
            position = projectManager.lstProjectItems.indexOf(drawingManager.projectItemManager) + 1;
            m1181g();
        }
    }

    public void moveToLast() {
        position = projectManager.lstProjectItems.size() - 1;
        m1181g();
    }

    private void showMoreOptions() {
        prepareBottomSheetData();
        ArrayList<BottomSheetData> lstOptions = prepareBottomSheetData();
        PopUtils.showOptionsWithItems(this, position, getString(R.string.select_option), lstOptions, (listPosition, position, item) -> doAfterProjectOptionSelected(position));
    }

    private ArrayList<BottomSheetData> prepareBottomSheetData() {
        ArrayList<BottomSheetData> lstOptions = new ArrayList<>();
        BottomSheetData sheetData = new BottomSheetData();
        sheetData.imageId = R.drawable.icn_edit_black;
        sheetData.title = getString(R.string.txt_re_add_item);
        lstOptions.add(sheetData);

        BottomSheetData sheetData1 = new BottomSheetData();
        sheetData1.imageId = R.drawable.icn_project_item;
        sheetData1.title = getString(R.string.copy_item);
        lstOptions.add(sheetData1);

        BottomSheetData sheetData2 = new BottomSheetData();
        sheetData2.imageId = R.drawable.icn_reset;
        sheetData2.title = getString(R.string.reset_item);
        lstOptions.add(sheetData2);

        BottomSheetData sheetData3 = new BottomSheetData();
        sheetData3.imageId = R.drawable.icn_project_detail;
        sheetData3.title = getString(R.string.item_detail);
        lstOptions.add(sheetData3);

        BottomSheetData sheetData4 = new BottomSheetData();
        sheetData4.imageId = R.drawable.icn_delete;
        sheetData4.title = getString(R.string.delete_item);
        lstOptions.add(sheetData4);

        return lstOptions;

    }

    /**
     * @param position project item position in list
     */
    private void doAfterProjectOptionSelected(int position) {
        switch (position) {
            case 0:
                editItem(drawingManager.projectItemManager);
                break;
            case 1:
                copyItem(drawingManager.projectItemManager);
                break;
            case 2:
                drawingManager.resetItem();
                break;
            case 3:
                PopUtils.showProjectItemDetail(this, getString(R.string.item_detail), DesignActivity.drawingManager.projectItemManager);
                break;
            case 4:
                deleteItemDialog();
                break;

        }
    }

    private void showCreateNewProjectDialog() {
        PopUtils.showCreateNewProjectItemDialog(this, getString(R.string.new_project_item), getString(R.string.txt_add), dialogData -> {
            createNewProjectItem(dialogData.data1, dialogData.data2, dialogData.data3);
        }, view -> {

        }, view -> {

        });
    }

    private void createNewProjectItem(String width, String height, String qty) {
        DesignActivity.drawingManager = new DrawingManager();
        DesignActivity.drawingManager.projectItemManager = DesignActivity.projectManager.createProjectItem(ProjectSubItemManager.getProjectSubItem());
        DesignActivity.drawingManager.projectItemManager.itemQuantity = Integer.valueOf(qty);
        DesignActivity.drawingManager.m1226a((float) Integer.parseInt(width), (float) Integer.parseInt(height));
        DesignActivity.projectManager.lstProjectItems.add(DesignActivity.drawingManager.projectItemManager);
        moveToLast();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.startDrag(ClipData.newPlainText("", view.getTag().toString()), new View.DragShadowBuilder(view), view, 0);
            return true;
        }

        return false;
    }

    @Override
    public boolean onDrag(View dragView, DragEvent dragEvent) {
        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            String drawTypeString = dragEvent.getClipData().getItemAt(0).getText().toString();

            DrawTypeEnum drawTypeEnum = DrawTypeEnum.valueOf(drawTypeString);

            if (drawTypeEnum == DrawTypeEnum.DoorOpen) {
                Toast.makeText(this, "In Development!", Toast.LENGTH_SHORT).show();
                return true;
            }

            DrawingPoints drawingPoints = DesignActivity.drawingManager.f744b.setDrawingPoints(new DrawingPoints(dragEvent.getX(), dragEvent.getY()));
            GlazingBead glazingBead = DesignActivity.drawingManager.projectItemManager.getGlazingBeadFromPoints(drawingPoints);

            if (glazingBead != null) {
                C0142t c0142t = DesignActivity.drawingManager.projectItemManager.m1289b().m1261d(glazingBead.positionCode);

                WingFunction wingFunction = new WingFunction();

                switch (drawTypeEnum) {
                    case TurnLeft:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.Turn;
                        wingFunction.directionsEnum1 = DirectionsEnum.left;
                        wingFunction.directionsEnum2 = DirectionsEnum.right;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case TurnRight:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.Turn;
                        wingFunction.directionsEnum1 = DirectionsEnum.right;
                        wingFunction.directionsEnum2 = DirectionsEnum.left;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case Tilt:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.Tilt;
                        wingFunction.directionsEnum1 = DirectionsEnum.up;
                        wingFunction.directionsEnum2 = DirectionsEnum.down;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case TiltTurnLeft:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.TiltTurn;
                        wingFunction.directionsEnum1 = DirectionsEnum.left;
                        wingFunction.directionsEnum2 = DirectionsEnum.right;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case TiltTurnRight:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.TiltTurn;
                        wingFunction.directionsEnum1 = DirectionsEnum.right;
                        wingFunction.directionsEnum2 = DirectionsEnum.left;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case DoorLeft:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.Door;
                        wingFunction.directionsEnum1 = DirectionsEnum.left;
                        wingFunction.directionsEnum2 = DirectionsEnum.right;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case DoorRight:
                        wingFunction.wingFunctionTypeEnum = WingFunctionTypeEnum.Door;
                        wingFunction.directionsEnum1 = DirectionsEnum.right;
                        wingFunction.directionsEnum2 = DirectionsEnum.left;
                        DesignActivity.drawingManager.addCasementData(wingFunction, glazingBead, c0142t);
                        break;
                    case SlidingRight:
                        DesignActivity.drawingManager.addSlidingDataForSingleSide(DrawTypeEnum.Sliding2A, glazingBead, c0142t);
                        break;
                    case SlidingLeft:
                        DesignActivity.drawingManager.addSlidingDataForSingleSide(DrawTypeEnum.Sliding2B, glazingBead, c0142t);
                        break;
                    case Sliding2A:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding2A, glazingBead, c0142t);
                        break;
                    case Sliding2C:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding2C, glazingBead, c0142t);
                        break;
                    case Sliding3F:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding3F, glazingBead, c0142t);
                        break;
                    case Sliding3H:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding3H, glazingBead, c0142t);
                        break;
                    case Sliding4A:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding4A, glazingBead, c0142t);
                        break;
                    case Sliding2B:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding2B, glazingBead, c0142t);
                        break;
                    case Sliding3A:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding3A, glazingBead, c0142t);
                        break;
                    case Sliding3G:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding3G, glazingBead, c0142t);
                        break;
                    case Sliding3I:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding3I, glazingBead, c0142t);
                        break;
                    case Sliding4B:
                        DesignActivity.drawingManager.addSlidingDataNew(DrawTypeEnum.Sliding4B, glazingBead, c0142t);
                        break;
                }

                C0156s a2 = c0142t.c0156s.m1325a(glazingBead.positionCode);

                switch (drawTypeEnum) {
                    case MullionHorizontal:
                        drawingManager.addSingleMullion(DrawOrientationEnum.Horizontal, Float.toString(a2.c0153o.horizontalFloat / 2.0f), glazingBead, c0142t);
                        break;
                    case MullionVerticalSingle:
                        drawingManager.addSingleMullion(DrawOrientationEnum.Vertical, Float.toString(a2.c0153o.verticalFloat / 2.0f), glazingBead, c0142t);
                        break;
                    case MullionVerticalDouble:
                        DesignActivity.drawingManager.addDoubleMullionData(a2, glazingBead, c0142t);
                        break;
                    case PanelHorizontal:
                        drawingManager.addPanelData(glazingBead, DrawOrientationEnum.Horizontal);
                        break;
                    case PanelVertical:
                        drawingManager.addPanelData(glazingBead, DrawOrientationEnum.Vertical);
                        break;
                }
            }
        }
        return true;
    }
}
