package com.example.GoodMoodJournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;

public class ListAdapter extends BaseAdapter {
    Context context;
    HashMap<Integer, Entry> data;
    Integer ids[];

    private static LayoutInflater inflater;

    public ListAdapter(Context context, HashMap<Integer, Entry> data) {
        this.context = context;
        this.data = data;
        ids = data.keySet().toArray(new Integer[data.size()]);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(ids[i]);
    }

    @Override
    public long getItemId(int i) {
        return ids[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null)
            vi = inflater.inflate(R.layout.list_row, null);
        Entry entry = data.get(ids[i]);
        TextView date = (TextView) vi.findViewById(R.id.date);
        TextView text = (TextView) vi.findViewById(R.id.excerpt);
        date.setText(entry.date);
        text.setText(entry.text);
        return vi;
    }
}
