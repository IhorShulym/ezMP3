package com.dtomic.ezmp3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class ListviewLayoutActivity extends ActionBarActivity {

    private String songName = null;
    SQLiteDatabase db = null;
    DBHelper helper;
    Cursor result = null;
    ArrayList<String> songs = new ArrayList<String>();
    ArrayList<String> ids = new ArrayList<String>();
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            songName = extras.getString("songName");
        }
        helper = new DBHelper(this);
        list = (ListView) findViewById(R.id.listView);
        AsyncWork aw = new AsyncWork();
        aw.execute();
    }

    private class AsyncWork extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... parameters) {
            if(songName != null) {
                try {
                    db = helper.getDB();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                result = db.rawQuery("SELECT ARTIST, TITLE, _id FROM SONGS WHERE TITLE=" + "\"" + songName + "\"" + "", null);
                result.moveToFirst();
                while(!result.isAfterLast()) {
                    String song = result.getString(0) + " - " + result.getString(1);
                    songs.add(song);
                    ids.add(result.getString(2));
                    result.moveToNext();
                }
            } else {
                try {
                    db = helper.getDB();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                result = db.rawQuery("SELECT ARTIST, TITLE, _id FROM SONGS", null);
                result.moveToFirst();
                while(!result.isAfterLast()) {
                    String song = result.getString(0) + " - " + result.getString(1);
                    songs.add(song);
                    ids.add(result.getString(2));
                    result.moveToNext();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListviewLayoutActivity.this, R.layout.listview_text, songs);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String identifier = ids.get(position);
                    Intent intent = new Intent(ListviewLayoutActivity.this, Player.class);
                    intent.putExtra("id", identifier);
                    startActivity(intent);
                }
            });
        }
    };
}
