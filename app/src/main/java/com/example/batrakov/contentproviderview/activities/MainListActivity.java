package com.example.batrakov.contentproviderview.activities;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.batrakov.contentproviderview.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Main app activity represents ListView and buttons for getting information
 * from Database of Foxes and Badgers. Allow to start second DetailsActivity
 * after clicking on list elements. Keeps last synchronization time stamp in
 * shared preferences and restore it after restart.
 */
public class MainListActivity extends AppCompatActivity {

    /**
     * Flag for clicked list element id for transferring through the intent to start DetailsActivity.
     */
    public static final String CLICKED_ITEM_ID = "item id";
    /**
     * Flag for clicked list element table type for transferring through the intent to start DetailsActivity.
     */
    public static final String CHOSEN_TYPE_URI = "type";

    private static final Uri FOXES_URI = Uri
            .parse("content://com.example.batrakov.contentproviderdatabase.authority/foxes");

    private static final Uri BADGERS_URI = Uri
            .parse("content://com.example.batrakov.contentproviderdatabase.authority/badgers");

    private static final String[] TARGET_TABLE_COLUMNS_NAME = {"_id", "name"};
    private static final int[] TARGET_LIST_ITEM_VIEWS = {android.R.id.text1, android.R.id.text2};
    private static final String SAVED_TIME_STAMP = "time stamp";
    private static final int FOXES_LOADER_ID = 0;
    private static final int BADGERS_LOADER_ID = 1;

    private TextView mTimeStamp;
    private SimpleCursorAdapter mFoxesCursorAdapter;
    private SimpleCursorAdapter mBadgersCursorAdapter;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
        setContentView(R.layout.main_list_layout);

        mTimeStamp = findViewById(R.id.timeStamp);
        ListView foxesListView = findViewById(R.id.list_foxes);
        ListView badgersListView = findViewById(R.id.list_badgers);

        findViewById(R.id.hack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aView) {
                hack();
            }
        });


        TabHost tabHost = findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("foxes");
        tabSpec.setIndicator(getString(R.string.foxes));
        tabSpec.setContent(R.id.list_foxes);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("badgers");
        tabSpec.setIndicator(getString(R.string.badgers));
        tabSpec.setContent(R.id.list_badgers);
        tabHost.addTab(tabSpec);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.contains(SAVED_TIME_STAMP)) {
            mTimeStamp.setText(preferences.getString(SAVED_TIME_STAMP, ""));
        }

        mFoxesCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_2,
                null, TARGET_TABLE_COLUMNS_NAME, TARGET_LIST_ITEM_VIEWS, 0);

        mBadgersCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_2,
                null, TARGET_TABLE_COLUMNS_NAME, TARGET_LIST_ITEM_VIEWS, 0);

        foxesListView.setAdapter(mFoxesCursorAdapter);
        badgersListView.setAdapter(mBadgersCursorAdapter);

        foxesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
                Intent startDetailActivityIntent = new Intent(getBaseContext(), DetailsActivity.class);
                startDetailActivityIntent.putExtra(CLICKED_ITEM_ID, aId);
                startDetailActivityIntent.putExtra(CHOSEN_TYPE_URI, FOXES_URI);
                startActivity(startDetailActivityIntent);
            }
        });

        badgersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
                Intent startDetailActivityIntent = new Intent(getBaseContext(), DetailsActivity.class);
                startDetailActivityIntent.putExtra(CLICKED_ITEM_ID, aId);
                startDetailActivityIntent.putExtra(CHOSEN_TYPE_URI, BADGERS_URI);
                startActivity(startDetailActivityIntent);
            }
        });


        getLoaderManager().initLoader(FOXES_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int aId, Bundle aArgs) {
                return new CursorLoader(getApplicationContext(), FOXES_URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> aLoader, Cursor aCursor) {
                mFoxesCursorAdapter.swapCursor(aCursor);

                Date currentTime = Calendar.getInstance().getTime();
                String timeStamp = currentTime.toString();
                mTimeStamp.setText(timeStamp);
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit()
                        .putString(SAVED_TIME_STAMP, mTimeStamp.getText().toString()).apply();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> aLoader) {
            }
        });

        getLoaderManager().initLoader(BADGERS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int aId, Bundle aArgs) {
                return new CursorLoader(getApplicationContext(), BADGERS_URI, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> aLoader, Cursor aData) {
                mBadgersCursorAdapter.swapCursor(aData);

                Date currentTime = Calendar.getInstance().getTime();
                String timeStamp = currentTime.toString();
                mTimeStamp.setText(timeStamp);
                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit()
                        .putString(SAVED_TIME_STAMP, mTimeStamp.getText().toString()).apply();
            }

            @Override
            public void onLoaderReset(Loader<Cursor> aLoader) {
            }
        });

    }

    private void hack() {

        Uri uri = FOXES_URI.buildUpon().appendPath("1").build();
        // 1 = 1; delete from foxes; delete from foxes where 1 = 1  " AND " + DBContract.Entry._ID + " = " + id;
        getContentResolver().query(uri, null, "_id = 1; drop table *; delete from foxes where _id = 1", null, null);
    }

//    /**
//     * Send request to Database and fill ListView by incoming information.
//     *
//     * @param aUri target Uri query.
//     *
//     * @throws SecurityException on content provider permission deny.
//     */
//    private void loadListFromDB(Uri aUri) throws SecurityException {
//
//        Date currentTime = Calendar.getInstance().getTime();
//        String timeStamp = currentTime.toString();
//        mTimeStamp.setText(timeStamp);
//
//        PreferenceManager.getDefaultSharedPreferences(this).edit()
//                .putString(SAVED_TIME_STAMP, mTimeStamp.getText().toString()).apply();
//
//        mCurrentListContentUri = aUri;
//
//        if (aUri.equals(FOXES_URI)) {
//            mListView.setAdapter(mFoxesCursorAdapter);
//        } else if (aUri.equals(BADGERS_URI)) {
//            mListView.setAdapter(mBadgersCursorAdapter);
//        }
//    }
}
