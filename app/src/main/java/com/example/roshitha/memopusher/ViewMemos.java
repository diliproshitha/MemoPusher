package com.example.roshitha.memopusher;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewMemos extends Activity {

    private static final String TAG = "ViewMemos";

    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memos);

        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        
        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "Populate List View");

        // Get data from database
        Cursor data = mDatabaseHelper.getData();

        ArrayList<String> listData = new ArrayList<>();

        while (data.moveToNext()) {
            // get data from database column by column
            // and add it to array list

            listData.add(data.getString(1));
        }

        // create the list adapter and set it to adapter
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, listData);
        mListView.setAdapter(adapter);
    }
}
