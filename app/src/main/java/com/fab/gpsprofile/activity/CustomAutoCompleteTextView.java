package com.fab.gpsprofile.activity;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

/** Customizing AutoCompleteTextView to return Place Description
 * corresponding to the selected item
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
 
    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        Color.parseColor("#F44336");
    }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}