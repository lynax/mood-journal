package com.example.GoodMoodJournal;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;

public class ListEntry extends Activity {
    private PostsDatabaseHelper db;
    private Integer limit = 10;
    private Integer offset = 0;
    private Integer total_entries;

    private Button prev;
    private Button next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        db = PostsDatabaseHelper.getInstance(this);
        total_entries = db.getCount();
        getEntries();
    }

    public void getEntries() {
        HashMap<Integer, Entry> entries = db.getEntries(limit, offset * limit);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new ListAdapter(this, entries));
        setButtonStyle();
    }

    public void setButtonStyle() {
        if(prev == null)
            prev = (Button) this.findViewById(R.id.prev);
        if(next == null)
            next = (Button) this.findViewById(R.id.next);

        if(offset > 0) {
            prev.setBackgroundColor(Color.parseColor("#ED6519"));
            prev.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            prev.setBackgroundColor(Color.parseColor("#000000"));
            prev.setTextColor(Color.parseColor("#888888"));
        }

        if(limit * (offset + 1) < total_entries) {
            next.setBackgroundColor(Color.parseColor("#ED6519"));
            next.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            next.setBackgroundColor(Color.parseColor("#000000"));
            next.setTextColor(Color.parseColor("#888888"));
        }
    }

    public void getPrev(View view) {
        if(offset > 0) {
            offset--;
            getEntries();
        }
    }

    public void getNext(View view) {
        if(limit * (offset + 1) <= total_entries) {
            offset++;
            getEntries();
        }
    }

}