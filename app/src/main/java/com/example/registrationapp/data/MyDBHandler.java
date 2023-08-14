package com.example.registrationapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.registrationapp.model.RegistrationPojo;
import com.example.registrationapp.params.Params;

public class MyDBHandler extends SQLiteOpenHelper {
    public MyDBHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table = "CREATE TABLE " + Params.DB_TABLE_NAME + "(" + Params.KEY_ID + " INTEGER PRIMARY KEY," +Params.KEY_Username + " TEXT," + Params.KEY_Email
                + " TEXT ," + Params.KEY_Password + " TEXT," + Params.KEY_Confirmpassword + " TEXT" + ")";
        Log.d("DB ","Query has been run is : "+create_table);
        //String
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

   /* public Boolean addRegistration(RegistrationPojo registration){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Params.KEY_Username, registration.getUserName());
        values.put(Params.KEY_Email,registration.getEmail());
        values.put(Params.KEY_Password,registration.getPassword());
        values.put(Params.KEY_Confirmpassword,registration.getConfirmPassword());


        long result = db.insert(Params.DB_TABLE_NAME, null, values);
        Log.d("DB ","Registration data succesfully inserted");
        db.close();
        if(result == -1){
            return false;
        }else {
            return true;
        }
    } */

    public Boolean insertData(String username, String email, String password, String confirmPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("confirmPassword", confirmPassword);
        long result = db.insert(Params.DB_TABLE_NAME, null, values);
        if (result == -1){
            return  false;
        }else{
            return true;
        }

    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ Params.DB_TABLE_NAME+ " where email =?", new String[] {email});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernameAndPassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ Params.DB_TABLE_NAME+ " where username =? and password = ?", new String[] {username,password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
