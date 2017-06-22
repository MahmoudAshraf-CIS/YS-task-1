package com.example.mannas.ytask.Content;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Mannas on 6/19/2017.
 */

public class OfflineContentProvider extends android.content.ContentProvider {
    DB_Helper mDBHelper;
    static final String LOG_TAG = OfflineContentProvider.class.getName();

    @Override
    public boolean onCreate() {
        mDBHelper = new DB_Helper(getContext(),"Ytasy_DB",null,1);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase DB = mDBHelper.getReadableDatabase();
        return DB.query(Contract.Feed.TABLE_NAME,projection,selection,selectionArgs, null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        mDBHelper.getWritableDatabase().insert(Contract.Feed.TABLE_NAME,null,values);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return mDBHelper.getWritableDatabase().delete(Contract.Feed.TABLE_NAME,selection,selectionArgs);

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
