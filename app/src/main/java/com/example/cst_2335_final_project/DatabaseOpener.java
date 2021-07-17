package com.example.cst_2335_final_project;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpener extends SQLiteOpenHelper {

    // Creates the static variables for our database.
    protected final static String DATABASE_NAME = "SavedImage_DB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "ImageData";
    public final static String COL_ID = "_id";
    public final static String COL_DATE = "Date";
    public final static String COL_EXPLANATION = "Explanation";
    public final static String COL_URL = "Url";
    public final static String COL_HD_URL = "UrlHd";
    public final static String COL_TITLE = "Title";
    public final static String COL_FILENAME = "FileName";


    //context is activity that is calling this function.
    public DatabaseOpener(Context ctx)
    {
        super(ctx,DATABASE_NAME,null, VERSION_NUM  );
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //executes our SQL statement to create the table to store messages in
        db.execSQL(   "CREATE TABLE " + TABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " //_id required for use with cursor
                +  COL_DATE + " TEXT,"
                +  COL_EXPLANATION + " TEXT,"
                +  COL_URL + " TEXT,"
                +  COL_HD_URL + " TEXT,"
                +  COL_TITLE + " TEXT,"
                +  COL_FILENAME + " TEXT);"
        );
    }

    //this function gets called if the database version on that is on the installed device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //this function gets called if the database version on is on the installed device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}