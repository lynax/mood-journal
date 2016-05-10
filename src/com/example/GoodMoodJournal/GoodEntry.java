package com.example.GoodMoodJournal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GoodEntry extends Activity {
    private PostsDatabaseHelper db;
    private String date_today;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_event);

        TextView title = (TextView) findViewById(R.id.date_today);
        date_today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        title.setText(date_today);
        db = PostsDatabaseHelper.getInstance(this);
    }

    public void addEntry(View view) {
        EditText text = (EditText) findViewById(R.id.text);
        Entry entry = new Entry();
        entry.date = date_today;
        entry.text = text.getText().toString().trim();
        if(entry.text.length() > 0) {
            db.addEntry(entry);
            Intent intent = new Intent(GoodEntry.this, ListEntry.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG).show();
        }
    }

}