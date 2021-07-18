package com.example.owner.combined;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME ="register.db";
    public static final String TABLE_NAME ="registeruser";
    public static final String COL_1 ="ID";
    public static final String COL_2 ="password";
    public static final String COL_3 ="number1";
    public static final String COL_5 ="verifyNum";
    public static final String COL_6 ="number1Pin";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY  KEY AUTOINCREMENT,  password TEXT,number1 TEXT,verifyNum TEXT,number1Pin TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long Verify( String number){
        SQLiteDatabase db = this.getWritableDatabase();
        String blank="null";
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",blank);
        contentValues.put("number1",blank);
        contentValues.put("verifyNum",number);
        contentValues.put("number1Pin",blank);
        long res = db.insert("registeruser",null,contentValues);
        db.close();
        return  res;
    }

    public boolean addUser( String password,String fnum,String fnumPin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);
        contentValues.put("number1",fnum);
        contentValues.put("number1Pin",fnumPin);
        db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{"1"});
        db.close();
        return  true;
    }

    public boolean checkUser(String password){
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection =  COL_2 + "=?";
        String[] selectionArgs = { password };
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0)
            return  true;
        else
            return  false;
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res =db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public boolean updateUser(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);
        db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{"1"});
       return true;
    }
       public boolean updateNumber(String number){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("number1",number);
            db.update(TABLE_NAME,contentValues,"ID = ?",new String[]{"1"});
            return true;


        }
}
