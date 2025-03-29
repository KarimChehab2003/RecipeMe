package com.example.jsonexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    public static String databaseName = "userDatabase";

    SQLiteDatabase userDatabase;

    public DBhelper(Context context) {
        super(context, databaseName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create User Table
        db.execSQL("create table user (userID INTEGER Primary key Autoincrement , name text , email text , password text)");

        // creating history table
        db.execSQL("CREATE TABLE history (userID INTEGER , recipeID INTEGER, recipeName TEXT, " +
                "FOREIGN KEY (userID) REFERENCES user(userID), " +
                "PRIMARY KEY (recipeID, userID))");

        // creating favorites table
        db.execSQL("CREATE TABLE favorites (userID INTEGER , recipeID INTEGER, recipeName TEXT, " +
                "FOREIGN KEY (userID) REFERENCES user(userID), " +
                "PRIMARY KEY (recipeID, userID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists history");
        db.execSQL("drop table if exists favorites");
        onCreate(db);
    }

    // sign up
    public void createNewUser (String name,String email,String password){

        ContentValues data = new ContentValues();

        data.put("name",name);
        data.put("email",email);
        data.put("password",password);

        userDatabase = getWritableDatabase();
        userDatabase.insert("user",null,data);
        userDatabase.close();
    }

    // for login
    public User getUser(String email,String password){
        User retrieved = null;

        userDatabase = getReadableDatabase();

        Cursor rtdata = userDatabase.rawQuery("select * from user where email = ? and password = ?" , new String []{email,password});

        if(rtdata.getCount() != 0){
            if (rtdata.moveToFirst()) {
                retrieved = new User(rtdata.getLong(0), rtdata.getString(1), rtdata.getString(2), rtdata.getString(3));
            }
        }
        rtdata.close();
        userDatabase.close();
        return retrieved;
    }

    // for history
    public Cursor getHistory(int id){

        userDatabase = getReadableDatabase();

        Cursor rtdata =userDatabase.rawQuery("select recipeID , recipeName from history where userID = ?" , new String []{String.valueOf(id)});

        if(rtdata.getCount() ==0){
            // no users with these information
            userDatabase.close();
            return null;
        }

        userDatabase.close();
        return rtdata;
    }

    public void addToHistory (int ParamrecipeID, int ParamuserID ,String ParamrecipeName){

        ContentValues recipeData = new ContentValues();

        recipeData.put("recipeID",ParamrecipeID);
        recipeData.put("userID",ParamuserID);
        recipeData.put("recipeName", ParamrecipeName);

        userDatabase = getWritableDatabase();
        userDatabase.insert("history",null,recipeData);
        userDatabase.close();

    }

    // for favorites
    public Cursor getFavorites(int id){

        userDatabase = getReadableDatabase();

        Cursor rtdata =userDatabase.rawQuery("select recipeID , recipeName from favorites where userID = ?" , new String []{String.valueOf(id)});

        if(rtdata.getCount() ==0){
            // no users with these information
            userDatabase.close();
            return null;
        }

        userDatabase.close();
        return rtdata;
    }

    public void addToFavorites (int recipeID , int userID , String recipeName){

        ContentValues recipeData = new ContentValues();

        recipeData.put("recipeID",recipeID);
        recipeData.put("userID",userID);
        recipeData.put("recipeName", recipeName);

        userDatabase = getWritableDatabase();
        userDatabase.insert("favorites",null,recipeData);
        userDatabase.close();
    }

    public void removeFromFavorites (long userID, long recipeID){
        userDatabase=getWritableDatabase();
        userDatabase.execSQL("Delete from favorites where userID = ? and recipeID = ? ",new Long[]{userID,recipeID});
    }

}
