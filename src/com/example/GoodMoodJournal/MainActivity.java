package com.example.GoodMoodJournal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void addGoodEntry(View view) {
        Intent intent = new Intent(MainActivity.this, GoodEntry.class);
        startActivity(intent);
    }

    public void listEntry(View view) {
        Intent intent = new Intent(MainActivity.this, ListEntry.class);
        startActivity(intent);
    }
}
