package com.example.menu.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDB extends SQLiteOpenHelper {
    public  static final String TABLE_NAME = "VIDEO";
    public MyDB(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)  {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME  + "( "
                + DBitem.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +DBitem.KEY_NAME+" TEXT NOT NULL,"
                + DBitem.KEY_PID + " TEXT NOT NULL, "
                + DBitem.KEY_NOTE + " TEXT, "
                +DBitem.TIME_STAMP+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                +")";

        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
    public boolean addItem(DBitem item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBitem.KEY_NAME,item.getName());
        cv.put(DBitem.KEY_PID,item.getVid());
        cv.put(DBitem.KEY_NOTE,item.getNote());

        long result = db.insert(TABLE_NAME,null,cv);

        if (result==-1){
            return false;
        }else {
            //Log.d("MyDB","Add");
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY "+DBitem.KEY_ID+" DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSingleItem(Long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE " + DBitem.KEY_ID + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void deleteData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_NAME +" WHERE "+DBitem.KEY_ID+" = "+id;
        db.execSQL(delete);
    }

    public void updateData(long id,DBitem item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                DBitem.KEY_NAME + " = '" + item.getName() +"', "+
                DBitem.KEY_PID + " = '" + item.getVid() +"', "+
                DBitem.KEY_NOTE + " = '" + item.getNote() +
                "' WHERE " + DBitem.KEY_ID + " = " + id ;
        db.execSQL(query);
    }

    public void swapData(long d,long t){
        SQLiteDatabase db = this.getWritableDatabase();
        DBitem itemd =new DBitem(),itemt =new DBitem();
        String queryd = "SELECT * FROM " + TABLE_NAME +" WHERE " + DBitem.KEY_ID + " = " + d;
        Cursor cursord = db.rawQuery(queryd, null);
        cursord.moveToPosition(0);
        itemd.setName(cursord.getString(cursord.getColumnIndex(DBitem.KEY_NAME)));
        itemd.setVid(cursord.getString(cursord.getColumnIndex(DBitem.KEY_PID)));
        itemd.setNote(cursord.getString(cursord.getColumnIndex(DBitem.KEY_NOTE)));
        String queryt = "SELECT * FROM " + TABLE_NAME +" WHERE " + DBitem.KEY_ID + " = " + t;
        Cursor cursort = db.rawQuery(queryt, null);
        cursort.moveToPosition(0);
        itemt.setName(cursort.getString(cursort.getColumnIndex(DBitem.KEY_NAME)));
        itemt.setVid(cursort.getString(cursort.getColumnIndex(DBitem.KEY_PID)));
        itemt.setNote(cursort.getString(cursort.getColumnIndex(DBitem.KEY_NOTE)));
        String query = "UPDATE " + TABLE_NAME + " SET " +
                DBitem.KEY_NAME + " = '" + itemd.getName() +"', "+
                DBitem.KEY_PID + " = '" + itemd.getVid() +"', "+
                DBitem.KEY_NOTE + " = '" + itemd.getNote() +
                "' WHERE " + DBitem.KEY_ID + " = " + t ;
        db.execSQL(query);
        query = "UPDATE " + TABLE_NAME + " SET " +
                DBitem.KEY_NAME + " = '" + itemt.getName() +"', "+
                DBitem.KEY_PID + " = '" + itemt.getVid() +"', "+
                DBitem.KEY_NOTE + " = '" + itemt.getNote() +
                "' WHERE " + DBitem.KEY_ID + " = " + d ;
        db.execSQL(query);
    }

}
