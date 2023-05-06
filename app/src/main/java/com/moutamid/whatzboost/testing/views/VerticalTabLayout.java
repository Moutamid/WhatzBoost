package com.moutamid.whatzboost.testing.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.moutamid.whatzboost.R;

public class VerticalTabLayout extends TabLayout {
    public VerticalTabLayout(Context context) {
        super(context);
        init();
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setRotation(-90);
        setLayoutDirection(LAYOUT_DIRECTION_RTL);
    }

    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        View customView = LayoutInflater.from(getContext())
                .inflate(R.layout.tab_item_vertical, null);
        TextView textView = customView.findViewById(android.R.id.text1);
        textView.setText(tab.getText());
        tab.setCustomView(customView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        customView.setLayoutParams(params);
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this) {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    TabLayout.Tab tab = getTabAt(position);
                    if (tab != null) {
                        View tabView = tab.getCustomView();
                        if (tabView != null) {
                            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                                    0, View.MeasureSpec.UNSPECIFIED);
                            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                                    getHeight(), View.MeasureSpec.EXACTLY);
                            tabView.measure(widthMeasureSpec, heightMeasureSpec);
                            tabView.layout(0, 0, tabView.getMeasuredWidth(),
                                    tabView.getMeasuredHeight());
                            tabView.setRotation(-90);
                        }
                    }
                }
            });
        }
    }
}
