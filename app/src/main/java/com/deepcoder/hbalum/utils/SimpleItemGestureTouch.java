package com.deepcoder.hbalum.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.deepcoder.hbalum.activities.DesignActivity;

public class SimpleItemGestureTouch implements View.OnTouchListener {
    GestureDetector gestureDetector;
    DesignActivity designActivity;

    public SimpleItemGestureTouch(DesignActivity designActivity, Context context) {
        this.designActivity = designActivity;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
                try {
                    float y = motionEvent2.getY() - motionEvent.getY();
                    float x = motionEvent2.getX() - motionEvent.getX();
                    if (Math.abs(x) > Math.abs(y)) {
                        if (Math.abs(x) > 100.0f && Math.abs(velocityX) > 100.0f) {
                            if (x > 0.0f) {
                                previousItem();
                            } else {
                                nextItem();
                            }
                        }
                    } else if (Math.abs(y) > 100.0f && Math.abs(velocityY) > 100.0f) {
                        if (y > 0.0f) {
                            doenMove();
                        } else {
                            upMove();
                        }
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    public void previousItem() {
        this.designActivity.previousItem();
    }

    public void nextItem() {
        this.designActivity.nextItem();
    }

    public void upMove() {
    }

    public void doenMove() {
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }
}
