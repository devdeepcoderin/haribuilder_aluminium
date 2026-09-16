package com.deepcoder.hbalum.utils;

import com.deepcoder.hbalum.BuildConfig;

import java.util.UUID;

public class StaticData {


    public static final String HINT_DONE = "hintShown";
    //Add all static data here
    public static int SCREEN_HEIGHT, SCREEN_WIDTH;

    public static String from = "from";
    public static String main = "main";
    public static final String DOUBLE_GLAZING = "Double Glazing";

    public static final String PRIVACY_POLICY_URL = BuildConfig.WEB_BASE_URL+ "privacy-policy.html";


    public static final String FILE_EXTENSION = ".af";

    public static final String ERROR_GENERAL_MESSAGE = "Something went wrong, Please try again!";

    public static final String PREF_IS_LOGGED_IN = "PREF_IS_LOGGED_IN";
    public static final String PREF_EMAIL_ID = "PREF_EMAIL_ID";
    public static final String PREF_API_ACCESS_TOKEN = "PREF_API_ACCESS_TOKEN";
    public static final String PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN";
    public static final String PREF_PAYMENT_STATUS = "PREF_payment_status";
    public static final String PREF_IS_TRIAL = "PREF_is_trial";
    public static final String PREF_TRIAL_DAYS = "PREF_trial_days";
    public static final String PREF_SUBSCRIPTION_STATUS = "PREF_SUBSCRIPTION_STATUS";
    public static final String PREF_SUBSCRIPTION_ACTIVE_ID = "PREF_SUBSCRIPTION_ACTIVE_ID";
    public static final String PREF_IS_APPLIED = "PREF_is_applied";

    public static final int REQUEST_CODE_VERIFY_PASSWORD = 101;

    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_LIST_WORKSHOP_ITEMS = "EXTRA_LIST_WORKSHOP_ITEMS";
    public static final String EXTRA_LIST_STORE_ITEMS = "EXTRA_LIST_STORE_ITEMS";
    public static final String EXTRA_PROJECT_ITEM_NAME = "EXTRA_PROJECT_ITEM_NAME";
    public static final String EXTRA_PROJECT_ITEM_THUMB = "EXTRA_PROJECT_ITEM_THUMB";
    public static final String EXTRA_PROJECT_ITEM_NUMBER = "itemName";
    public static final String EXTRA_PROJECT_ITEM_QUANTITY = "itemQuantity";

    public static final String EXTRA_VERIFICATION_MODE = "EXTRA_VERIFICATION_MODE";
    public static final int EMAIL_VERIFICATION = 1;
    public static final int PASSWORD_VERIFICATION = 0;

    public static final int OTP_TESTING_MODE_ON = 1;
    public static final int OTP_TESTING_MODE_OFF = 0;
    public static final int EXTRA_OTP_TESTING_MODE = OTP_TESTING_MODE_ON;
    public static final UUID ALUMINIMUM_UUID = UUID.fromString("5ed76137-2e1a-41f1-ad37-b4124ecf9970");
    public static final String MATERIAL_TYPE = "Aluminium";

    public static final String DRAWING_ITEM_FRAME = "Frame";
    public static final String DRAWING_ITEM_MULLION = "Mullion";
    public static final String DRAWING_ITEM_WINDOW_SASH = "Window Open";
    public static final String DRAWING_ITEM_DOOR_SASH = "Door Sash";
    public static final String DRAWING_ITEM_PANEL = "Panel";
    public static final String DRAWING_ITEM_DOUBLE_GLAZING_BEAD = "Double Glazing Bead";

    public static final String DRAWING_ITEM_SLIDING_FRAME = "Sliding Frame";
    public static final String DRAWING_ITEM_SLIDING_SASH = "Sliding Sash";
    public static final String DRAWING_ITEM_SLIDING_DOUBLE_GLAZING_BEAD = "Sliding Double Glazing Bead";
    public static final String DRAWING_ITEM_INTERLOCK = "Interlock";
    public static final String DRAWING_ITEM_FRONT_LOCKING = "Front Locking";

    public static final float VALUE_FRAME_PADDING = 19.0f;
    public static final float VALUE_MULLION_PADDING = 18.0f;
    public static final float VALUE_WINDOW_OPEN_MARGIN = 7.0f;
    public static final float VALUE_WINDOW_OPEN_PADDING = 27.5f;

    public static final int REQUEST_CODE_ASK_PERMISSION = 9991;

    // Calculations Data
    public static final int PROJECT_OPENNING_DIFF = 12;
}

