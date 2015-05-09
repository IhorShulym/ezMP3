package com.dtomic.ezmp3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


public class Player extends ActionBarActivity {
    Handler handler = new Handler();
    TextView from, to, songArtist, songTitle;
    ImageButton play, pause;
    SeekBar seekBar;
    Cursor result = null;
    String genre = null;
    String id = null;
    SQLiteDatabase db = null;
    DBHelper helper;
    boolean suspended = false;
    int count;
    Integer current;
    Integer previous;
    ArrayList<String> artist = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> duration = new ArrayList<String>();

    /*
    Initialize instance variables, call asyncworker
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_main);
        helper = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            genre = extras.getString("genre");
            id = extras.getString("id");
        }
        AsyncWork aw = new AsyncWork();
        aw.execute();
    }
    /*
    Initialize instance variables, call DatabaseHelper and retrieve data
    Store retrieved data in ArrayLists
     */
    private class AsyncWork extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... parameters) {
            if(genre != null) {
                from = (TextView) findViewById(R.id.from);
                to = (TextView) findViewById(R.id.to);
                songArtist = (TextView) findViewById(R.id.artist);
                songTitle = (TextView) findViewById(R.id.title);
                seekBar = (SeekBar) findViewById(R.id.seekBar);
                seekBar.setClickable(false);
                play = (ImageButton) findViewById(R.id.play);
                pause = (ImageButton) findViewById(R.id.pause);
                play.setEnabled(false);
                pause.setEnabled(true);
                try {
                    db = helper.getDB();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                result = db.rawQuery("SELECT ARTIST, TITLE, DURATION FROM SONGS WHERE GENRE=" + "\"" + genre + "\"" + "", null);
                result.moveToFirst();
                while (!result.isAfterLast()) {
                    artist.add(result.getString(0));
                    title.add(result.getString(1));
                    duration.add(result.getString(2));
                    result.moveToNext();
                }
                count = artist.size();
            } else if(id != null) {
                from = (TextView) findViewById(R.id.from);
                to = (TextView) findViewById(R.id.to);
                songArtist = (TextView) findViewById(R.id.artist);
                songTitle = (TextView) findViewById(R.id.title);
                seekBar = (SeekBar) findViewById(R.id.seekBar);
                seekBar.setClickable(false);
                play = (ImageButton) findViewById(R.id.play);
                pause = (ImageButton) findViewById(R.id.pause);
                play.setEnabled(false);
                pause.setEnabled(true);
                try {
                    db = helper.getDB();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                result = db.rawQuery("SELECT ARTIST, TITLE, DURATION FROM SONGS WHERE _id=" + "\"" + id + "\"" + "", null);
                result.moveToFirst();
                while (!result.isAfterLast()) {
                    artist.add(result.getString(0));
                    title.add(result.getString(1));
                    duration.add(result.getString(2));
                    result.moveToNext();
                }
                count = artist.size();
            }
            return null;
        }
        /*
        Call player
        */
        @Override
        protected void onPostExecute(Void args) {
            try {
                int pos = random(0, count-1);
                play(pos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    public static int random(int min, int max) {
        Random r = new Random();
        int num = r.nextInt((max - min) + 1) + min;
        return num;
    }
    /*
     Play random song
    */
    public void play(int pos) throws InterruptedException {
        current = pos;
        songArtist.setText(artist.get(current));
        songTitle.setText(title.get(current));
        seekBar.setProgress(0);
        from.setText("0:00");
        int dur = Integer.parseInt(duration.get(current));
        String fin = null;
        if((dur % 60) < 10) {
            fin = "0" + (dur % 60);
        } else {
            fin = "" + (dur % 60);
        }
        to.setText(dur / 60 + ":" + fin);
       //handler.postDelayed(Update, 1000);
    }
    //This part would have been implemented if not only high fidelity was required
    //Cosequently, there is no sense updating a seekbar if no real progress is being made
    //A true implementation of the player might come in the future, if needed
    /*
    private Runnable Update = new Runnable() {
        public void run() {
            int dur = Integer.parseInt(duration.get(current));
            int progressMin = 0;
            int progressSec = 0;
            while(dur > 0) {
                while(suspended) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(progressSec < 60) {
                    ++progressSec;
                } else {
                    ++progressMin;
                    progressSec = 0;
                }
                String progressSecString = null;
                if(progressSec < 10) {
                    progressSecString = "0" + progressSec;
                } else {
                    progressSecString = "" + progressSec;
                }
                from.setText(progressMin + ":" + progressSecString);
                seekBar.setProgress((progressMin*60) + progressSec);
                --dur;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void suspend(View view) {
        suspended = true;
        play.setEnabled(true);
        pause.setEnabled(false);
    }

    public void resume(View view) {
        suspended = false;
        play.setEnabled(false);
        pause.setEnabled(true);
        notify();
    }
    */
    /*
     Switch to next song metadata
    */
    public void next(View view) throws InterruptedException {
        previous = current;
        int pos = random(0, count-1);
        play(pos);
    }
    /*
    Switch to previous song metadata
    */
    public void prev(View view) throws InterruptedException {
        if(previous != null) {
            play(previous);
        }
    }
    /*
    Switch ot main activity
    */
    public void back(View view) {
        db.close();
        startActivity(new Intent(this, MainActivity.class));
    }

    /*
    Display songs
    */
    public void listSongs(View view) {
        db.close();
        Intent intent = new Intent(this, ListviewLayoutActivity.class);
        startActivity(intent);
    }
}
