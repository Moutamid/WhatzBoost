package com.moutamid.whatzboost.views;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.google.android.material.tabs.TabLayout;
import com.moutamid.whatzboost.R;

public class RotatedTab extends TabLayout.Tab {
    private final View parentView;

    public RotatedTab(View parentView) {
        super();
        this.parentView = parentView;
        setCustomView();
    }

    public RotatedTab setCustomView() {
        View tabView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.tab_item_vertical, null);
        tabView.setRotation(-90);
        super.setCustomView(tabView);
        return this;
    }

    public RotatedTab setCustomView(View view) {
        view.setRotation(-90);
        super.setCustomView(view);
        return this;
    }
}
