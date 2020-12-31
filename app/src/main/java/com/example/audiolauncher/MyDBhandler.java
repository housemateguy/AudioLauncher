package com.example.audiolauncher;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBhandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Login3.db";
    public static final String TABLE_NAME = "Login";
    public static final String COLUMN_ID = "LoginID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_MAIL = "Mail";
    public static final String COLUMN_NUMBER = "Number";
    //initialize the database



    public MyDBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
     /*   String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INT ,"+ COLUMN_NAME + " TEXT ,"+ COLUMN_MAIL + " TEXT ,"+COLUMN_NUMBER + " INT )";
        db.execSQL(CREATE_TABLE);*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}
    public String loadHandler() {
        String result = "";
        String query = "Select*FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            result += String.valueOf(result_0) + " " + result_1 +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        return result;
    }
    public void addHandler(Login login) {
        ContentValues values = new ContentValues();
       values.put(COLUMN_ID, login.getID());
        values.put(COLUMN_NAME, login.getName());
        values.put(COLUMN_MAIL, login.getMail());
        values.put(COLUMN_NUMBER, login.getNumber());


        SQLiteDatabase db = this.getWritableDatabase();
     /*   db.execSQL("INSERT INTO Login(LoginID,Name,Mail,Number) VALUES (`"+login.getID()+"`,`"+login.getName()+"`,`"+login.getMail()+"`,`"+login.getNumber()+"`)");
      */  db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Login findHandler(String name) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = " + "'" + name + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Login Login = new Login();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            Login.setID(Integer.parseInt(cursor.getString(0)));
            Login.setName(cursor.getString(1));
            Login.setMail(cursor.getString(2));
            Login.setNumber(cursor.getInt(3));

            cursor.close();
        } else {
            Login = null;
        }
        db.close();
        return Login;
    }
    public ArrayList<String> getAllHandler()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> noteList = new ArrayList<String>(500);
        String query = "Select * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.moveToNext() && !cursor.isAfterLast()) {
            noteList.add("NAME: "+cursor.getString(1)+", EMAIL: "+cursor.getString(2)+", NUMERO: "+cursor.getString(3));
            } cursor.close();



        return noteList;
    }

    public boolean deleteHandler(int ID) {
        boolean result = false;
        String query = "Select*FROM" + TABLE_NAME + "WHERE" + COLUMN_ID + "= '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Login student = new Login();
        if (cursor.moveToFirst()) {
            student.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[] {
                String.valueOf(student.getID())
            });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean updateHandler(int ID, String name, String mail, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, ID);
        args.put(COLUMN_NAME, name);
        args.put(COLUMN_MAIL, mail);
        args.put(COLUMN_NUMBER, number);


        return db.update(TABLE_NAME, args, COLUMN_ID + "=" + ID, null) > 0;
    }
}