package com.example.thirtyseven.myschedule;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class LessonCustom extends ArrayAdapter<Schedule> {

    private Context context;

    public LessonCustom(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList lesson) {
        super(context, 0, lesson);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.lesson_custom, parent, false);
        }
        return listViewItem;
    }
}
