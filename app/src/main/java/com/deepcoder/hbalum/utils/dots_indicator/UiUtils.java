package com.deepcoder.hbalum.utils.dots_indicator;

import android.content.Context;
import android.util.TypedValue;

import com.deepcoder.hbalum.R;

public class UiUtils {
  public static int getThemePrimaryColor(final Context context) {
    final TypedValue value = new TypedValue();
    context.getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, value, true);
    return value.data;
  }
}
