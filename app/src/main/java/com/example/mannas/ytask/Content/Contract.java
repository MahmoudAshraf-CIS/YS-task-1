package com.example.mannas.ytask.Content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mannas on 6/19/2017.
 */

public class Contract {



    public static final String Authority = "com.example.mannas.ytask";
    public static final Uri Base_content_URI = Uri.parse("content://"+Authority);

    public static class Feed implements BaseColumns {
        public static final String TABLE_NAME = "Feed";
        public static final String PATH = TABLE_NAME;
        public static final Uri uri = Base_content_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static class Columns {
            public static final String id = "id";
            public static final String json ="json";
        }

        public static final String CreationSQL_DDL =
                "CREATE TABLE " + TABLE_NAME + " ( "+
                        Columns.id + " INT PRIMARY KEY ON CONFLICT REPLACE , " +
                        Columns.json + " TEXT );";

        public static final String DropSQL_DDL = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    }
}
