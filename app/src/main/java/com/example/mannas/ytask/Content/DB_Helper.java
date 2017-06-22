package com.example.mannas.ytask.Content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mannas on 6/19/2017.
 */

public class DB_Helper extends SQLiteOpenHelper {

    public static final String LOG_TAG = DB_Helper.class.getName();

    public DB_Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.Feed.CreationSQL_DDL);
        Log.i(LOG_TAG,"new DataBase is deployed !");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){

            db.execSQL(  Contract.Feed.DropSQL_DDL);
            Log.i(LOG_TAG,"SQL DataBase is Dropped !");
            onCreate(db);
        }
    }
}
