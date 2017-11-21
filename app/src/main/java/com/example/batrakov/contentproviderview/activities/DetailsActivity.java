package com.example.batrakov.contentproviderview.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.batrakov.contentproviderview.R;

/**
 * Activity represents detail information about single element from MainActivity incoming list element.
 */
public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.details_layout);

        TextView typeView = findViewById(R.id.type);
        TextView nameView = findViewById(R.id.name);
        TextView ageView = findViewById(R.id.age);

        int clickedElementPosition = (int) getIntent().getLongExtra(MainListActivity.CLICKED_ITEM_ID, -1);

        if (clickedElementPosition != -1) {
            Uri uri = getIntent().getParcelableExtra(MainListActivity.CHOSEN_TYPE_URI);

            String type = uri.getLastPathSegment();

            uri = uri.buildUpon().appendPath(String.valueOf(clickedElementPosition)).build();
            Cursor cursor = getContentResolver().query(uri, null, null,
                    null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                typeView.setText(type);
                nameView.setText(cursor.getString(1));
                ageView.setText(cursor.getString(2));
                cursor.close();
            }

        }
    }

}
