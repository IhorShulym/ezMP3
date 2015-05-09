package com.dtomic.ezmp3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    EditText search;
    private Button random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.random = (Button) findViewById(R.id.random);
        random.setOnClickListener(this);
        search = (EditText) findViewById(R.id.search);
    }

    public void poprock(View view) {
        String genre = "poprock";
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }

    public void electronic(View view) {
        String genre = "electronic";
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }

    public void blues(View view) {
        String genre = "blues";
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }

    public void rb(View view) {
        String genre = "rb";
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }

    public void jazz(View view) {
        String genre = "jazz";
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }

    public void reggae(View view) {
        String genre = "reggae";
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }

    public static int random(int min, int max) {
        Random r = new Random();
        int num = r.nextInt((max - min) + 1) + min;
        return num;
    }
    /*
    Executed when search input is existent
    Gives a list of songs matching the search criteria
     */
    public void playSong(View view) {
        String songName = search.getText().toString();
        if(!songName.isEmpty()) {
            Intent intent = new Intent(this, ListviewLayoutActivity.class);
            intent.putExtra("songName", songName);
            startActivity(intent);
        }
    }
    /*
    Plays a random genre
     */
    public void onClick(View view) {
        ArrayList<String> genres = new ArrayList<String>();
        genres.add("poprock");
        genres.add("electronic");
        genres.add("blues");
        genres.add("rb");
        genres.add("jazz");
        genres.add("reggae");
        int max = genres.size();
        int start = random(0, max-1);
        String genre = genres.get(start);
        Intent intent = new Intent(this, Player.class);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }
}
