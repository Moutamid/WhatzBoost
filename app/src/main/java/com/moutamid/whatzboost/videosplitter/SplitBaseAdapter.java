package com.moutamid.whatzboost.videosplitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moutamid.whatzboost.R;

import java.util.ArrayList;

public class SplitBaseAdapter extends BaseAdapter {
    int position;
    private final ArrayList<String> arrayList = new ArrayList<>();
    private final LayoutInflater inflater;

    private class TextSetterClass {
        TextView textView;

        private TextSetterClass() {
        }

        TextSetterClass(SplitBaseAdapter splitBaseAdapter, SplitBaseAdapter splitBaseAdapter2, TextSetterClass aVar) {
            this();
        }
    }

    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"WrongConstant"})
    public SplitBaseAdapter(Context context, ArrayList<String> arrayList, int i) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList.addAll(arrayList);
        this.position = i;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        TextSetterClass aVar;
        if (view == null) {
            view = this.inflater.inflate(R.layout.videosplit_row_list, null);
            aVar = new TextSetterClass(this, this, null);
            aVar.textView = view.findViewById(R.id.textFormat);
            view.setTag(aVar);
        } else {
            aVar = (TextSetterClass) view.getTag();
        }
        aVar.textView.setText(this.arrayList.get(i));
        return view;
    }
}
