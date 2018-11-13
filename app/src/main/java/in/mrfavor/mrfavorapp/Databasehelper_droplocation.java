package in.mrfavor.mrfavorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by krishna on 5/14/2018.
 */

public class Databasehelper_droplocation extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "locations_droplocation.db";
    public static final String TABLE_NAME = "fav";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "location_name";
    public static final String COL_3 = "lat";
    public static final String COL_4 = "lng";

    public Databasehelper_droplocation(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,location_name TEXT,lat TEXT,lng TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String locationname,String lat,String lng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,locationname);
        contentValues.put(COL_3,lat);
        contentValues.put(COL_4,lng);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }







    public boolean isExist(String lname )
    {
        boolean ret = true;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME+" where location_name = '"+lname+"'",null);
        if(res.getCount() == 0) {
            ret = false;
        }

        return  ret;
    }

    public boolean updateData(String id,String locationname,String lat,String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,locationname);
        contentValues.put(COL_3,lat);
        contentValues.put(COL_4,lng);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

}
