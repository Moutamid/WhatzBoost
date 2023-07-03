package com.moutamid.whatzboost.listners;

import android.view.View;
import android.widget.TextView;

public interface SearchLister {
    void click(String name, boolean showAd, View dot, TextView view_counter);
}
