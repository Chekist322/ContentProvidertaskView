package com.example.batrakov.contentproviderview.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.batrakov.contentproviderview.R;

/**
 * Activity represents detail information about single element from MainActivity incoming list element.
 */
public class DetailsActivity extends AppCompatActivity {

    private static final String FOXES = "foxes";

    @Override
    protected void onCreate(@Nullable Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.details_layout);

        TextView typeView = findViewById(R.id.type);
        TextView nameView = findViewById(R.id.name);
        TextView ageView = findViewById(R.id.age);
        TextView colorView = findViewById(R.id.color);
        TextView colorLabel = findViewById(R.id.color_label);

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

                if (type.equals(FOXES)) {
                    String color = cursor.getString(3);
                    colorView.setVisibility(View.VISIBLE);
                    colorLabel.setVisibility(View.VISIBLE);
                    colorView.setText(color);
                } else {
                    colorView.setVisibility(View.GONE);
                    colorLabel.setVisibility(View.GONE);
                }
                cursor.close();
            }

        }
    }

}
