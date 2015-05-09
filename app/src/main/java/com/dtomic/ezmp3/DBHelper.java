package com.dtomic.ezmp3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;


public class DBHelper extends SQLiteOpenHelper {

    private static String PATH = "/data/data/com.dtomic.ezmp3/databases/";
    private static String NAME = "songs.db";
    private SQLiteDatabase db;
    private final Context ctxt;

    public DBHelper(Context context) {
        super(context, NAME, null, 1);
        this.ctxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int o, int n) { }
    /*
    Returns a SQLiteDatabase instance
    */
    public SQLiteDatabase getDB() throws IOException, SQLException {
        create();
        open();
        return this.db;
    }
    /*
    Creates a readable database in the databases folder, if not already existent
    */
    public void create() throws IOException {
        if(check()) {

        } else {
            this.getReadableDatabase();
            try {
                copy();
            } catch(IOException e) {
                throw new Error("Songs unavailable!");
            }
        }
    }
    /*
    Checks database existence
    */
    public boolean check() {
        boolean db = false;
        try {
            String path = ctxt.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator + NAME;
            File file = new File(path);
            db = file.exists();
        } catch(SQLiteException e) {
            System.out.println("No database at this location!");
        }
        return db;
    }
    /*
    Copies custom database from assets, to readable database in databases folder
    */
    public void copy() throws IOException {
        InputStream input = ctxt.getAssets().open(NAME);
        String out = PATH + NAME;
        OutputStream output = new FileOutputStream(out);
        byte[] buff = new byte[1024];
        int i;
        while((i = input.read(buff)) > 0) {
            output.write(buff, 0, i);
        }
        output.flush();
        output.close();
        input.close();
    }
    /*
    Opens database
    */
    public void open() throws SQLException {
        String path = PATH + NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }
    /*
    Closes database
   */
    @Override
    public synchronized void close() {
        if(db != null) {
            db.close();
        }
        super.close();
    }
}
