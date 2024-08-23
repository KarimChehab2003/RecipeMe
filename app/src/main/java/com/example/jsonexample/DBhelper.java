package com.example.jsonexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {

    public static String databaseName = "userDatabase";

    SQLiteDatabase userDatabase;


    public DBhelper(@Nullable Context context) {
        super(context, databaseName, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user (id int PRIMARY KEY AUTOINCREMENT ,  name TEXT , email TEXT , password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }
    // sign up
    public long createNewUser (String name,String email,String password){

        ContentValues data = new ContentValues();

        data.put("name",name);
        data.put("email",email);
        data.put("password",password);


        userDatabase = getWritableDatabase();
        long id  = userDatabase.insert("user",null,data);
        return id;
    }
    // for login
    public User getUser(String email,String password){
        User retrieved = null;

        userDatabase = getReadableDatabase();

        Cursor rtdata =userDatabase.rawQuery("select * from user where email = ? and password = ?" , new String []{email,password});


        if(rtdata.getCount() ==0){
            // no users with these information

            return null;
        }else{

            retrieved = new User(rtdata.getLong(0), rtdata.getString(1),rtdata.getString(2),rtdata.getString(3));


        }

        return retrieved;

    }

}
