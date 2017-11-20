package com.example.batrakov.contentproviderview.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.batrakov.contentproviderview.R;

/**
 * Created by batrakov on 15.11.17.
 */

public class DetailsActivity extends AppCompatActivity {


    private TextView mType;
    private TextView mName;
    private TextView mAge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);

        mType = findViewById(R.id.type);
        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);

        int clickedElementPosition = (int) getIntent().getLongExtra(MainListActivity.CLICKED_ITEM_ID, -1);

        if (clickedElementPosition != -1) {
            Uri uri = getIntent().getParcelableExtra(MainListActivity.CHOSEN_TYPE_URI);
            uri = uri.buildUpon().appendPath(String.valueOf(clickedElementPosition)).build();
            Cursor cursor = getContentResolver().query(uri, null, null,
                    null, null);
            cursor.moveToFirst();
            System.out.println(cursor.getString(1));
        }
    }

}
