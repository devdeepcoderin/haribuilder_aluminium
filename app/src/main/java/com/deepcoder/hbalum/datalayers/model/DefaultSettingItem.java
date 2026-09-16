package com.deepcoder.hbalum.datalayers.model;

public class DefaultSettingItem {

    private String tag, hint;
    private float defaultValue;

    public DefaultSettingItem(String tag, String hint, float value) {
        this.tag = tag;
        this.hint = hint;
        this.defaultValue = value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }
}