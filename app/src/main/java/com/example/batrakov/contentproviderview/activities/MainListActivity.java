package com.example.batrakov.contentproviderview.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.batrakov.contentproviderview.R;

import java.util.Calendar;
import java.util.Date;

public class MainListActivity extends AppCompatActivity {

    public static final String CLICKED_ITEM_ID = "item id";
    public static final String CHOSEN_TYPE_URI = "type";

    private static final Uri FOXES_URI = Uri
            .parse("content://com.example.batrakov.contentproviderdatabase.authority/foxes");

    private static final Uri BADGERS_URI = Uri
            .parse("content://com.example.batrakov.contentproviderdatabase.authority/badgers");

    private static final String sFromTableFieldsFrom[] = { "_id", "name" };
    private static final int sTargetListViews[] = { android.R.id.text1, android.R.id.text2 };
    private Uri mCurrentListContentUri;

    private Button mFoxesButton;
    private Button mBadgersButton;
    private TextView mTimeStamp;
    private ListView mListView;
    private SimpleCursorAdapter mCursorAdapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_layout);

        mFoxesButton = findViewById(R.id.foxes);
        mBadgersButton = findViewById(R.id.badgers);
        mListView = findViewById(R.id.list);
        mTimeStamp = findViewById(R.id.timeStamp);

        mFoxesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListFromDB(FOXES_URI);
            }
        });

        mBadgersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListFromDB(BADGERS_URI);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "position " + String.valueOf(position), Toast.LENGTH_SHORT).show();

                Intent startDetailActivityIntent = new Intent(getBaseContext(), DetailsActivity.class);
                startDetailActivityIntent.putExtra(CLICKED_ITEM_ID, id);
                startDetailActivityIntent.putExtra(CHOSEN_TYPE_URI, mCurrentListContentUri);
                startActivity(startDetailActivityIntent);
            }
        });
    }

    private void loadListFromDB(Uri aUri) {
        Cursor cursor = getContentResolver().query(aUri, null, null,
                null, null);
        mCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_2,
                cursor, sFromTableFieldsFrom, sTargetListViews, 0);
        mListView.setAdapter(mCursorAdapter);

        Date currentTime = Calendar.getInstance().getTime();
        String timeStamp = currentTime.toString();
        mTimeStamp.setText(timeStamp);

        mCurrentListContentUri = aUri;
    }


}
