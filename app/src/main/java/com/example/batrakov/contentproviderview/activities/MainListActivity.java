package com.example.batrakov.contentproviderview.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.batrakov.contentproviderview.R;

public class MainListActivity extends AppCompatActivity {

    private static final Uri FOXES_URI = Uri
            .parse("content://com.example.batrakov.contentproviderdatabase.authority/foxes");

    private static final Uri BADGERS_URI = Uri
            .parse("content://com.example.batrakov.contentproviderdatabase.authority/badgers");

    private static final String CLICKED_ITEM_ID = "item id";
    private static final int CLICKED_ITEM_URI = 1;

    Button mFoxesButton;
    Button mBadgersButton;
    ListView mListView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_layout);

        mFoxesButton = findViewById(R.id.foxes);
        mBadgersButton = findViewById(R.id.badgers);
        mListView = findViewById(R.id.list);

        mFoxesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(FOXES_URI, null, null,
                        null, null);
                String from[] = { "_id", "name" };
                int to[] = { android.R.id.text1, android.R.id.text2 };
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, cursor, from, to, 0);

                mListView.setAdapter(adapter);
            }
        });

        mBadgersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(BADGERS_URI, null, null,
                        null, null);
                String from[] = { "_id", "name" };
                int to[] = { android.R.id.text1, android.R.id.text2 };
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, cursor, from, to, 0);

                mListView.setAdapter(adapter);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "id " + String.valueOf(id), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                intent.putExtra(CLICKED_ITEM_ID, id);
            }
        });
    }
}
