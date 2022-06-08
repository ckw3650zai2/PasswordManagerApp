package my.edu.utar.passwordmanager;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteAdapter {

    public static final String MY_DATABASE_NAME = "Password_Manager.db";
    public static final String MY_DATABASE_TABLE = "Account";
    public static final String MY_DATABASE_TABLE2 = "SavedLists";

    public static final int MY_DATABASE_VERSION = 10;
    public static final String KEY_CONTENT = "Website";
    public static final String KEY_CONTENT_1 = "Username";
    public static final String KEY_CONTENT_2 = "Password";


    private static final String SCRIPT_CREATE_DATABASE = "create table " +
            MY_DATABASE_TABLE + " (username text primary key, " +
            "password text not null);";

    private static final String SCRIPT_CREATE_DATABASE2 = "create table " +
            MY_DATABASE_TABLE2 + " (id int primary key, " +
            "Website text not null, " + "Username text not null, "+ "Password text not null);";



    private Context context;
    public SQLiteHelper sqLiteHelper;
    public SQLiteDatabase sqLiteDatabase;

    //one parameter constructor

    public SQLiteAdapter(Context c){
        context = c;
    }

    //to read data from the table
    public SQLiteAdapter openToRead() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context,MY_DATABASE_NAME,null,MY_DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    //to write data into the table

    public SQLiteAdapter openToWrite() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context,MY_DATABASE_NAME,null,MY_DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }


    //to close the database
    public void close(){
        sqLiteHelper.close();
    }

    //to clear all rows in table

    public void removeData(){
        sqLiteDatabase.execSQL("delete from "+ MY_DATABASE_TABLE2);
    }

    //delete all my data in the MY_TABLE
    public int deleteAll(){
        return sqLiteDatabase.delete(MY_DATABASE_TABLE2,null,null);
    }



    //check username
    public Boolean checkusername(String user){
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from Account where username = ?", new String[]{user});
        if(cursor.getCount()>0){
            return true;
        }
        else
            return false;
    }

    //check username and password
    public Boolean checkusernamepassword(String username, String password){
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from Account where username = ? and password =? ", new String[]{username,password});
        if(cursor.getCount()>0){
            return true;
        }
        else
            return false;
    }

    //method overloading 2
    public long insert(String content, String content_2){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT,content);
        contentValues.put(KEY_CONTENT_2,content_2);
        return sqLiteDatabase.insert(MY_DATABASE_TABLE,null,contentValues);
    }


    //Check user

    //to extract the data from the table
    public List<WebObj> displayLists() {
        String[] columns = new String[] {"id", KEY_CONTENT,KEY_CONTENT_2,KEY_CONTENT_1};
        Cursor cursor = sqLiteDatabase.query(MY_DATABASE_TABLE2, columns, null, null, null, null, null);
        List<WebObj> result  = new ArrayList<WebObj>();
        int index_CONTENT_id = cursor.getColumnIndex("id");
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_1 = cursor.getColumnIndex(KEY_CONTENT_1);


        for (cursor.moveToFirst(); !(cursor.isAfterLast());
             cursor.moveToNext()) {
            WebObj webObj = new WebObj(cursor.getInt(index_CONTENT_id),cursor.getString(index_CONTENT),cursor.getString(index_CONTENT_2),cursor.getString(index_CONTENT_1));

            result.add(webObj);
        }

        return result;
    }

    // to delete the particular data

    public int deleteData (int id){

       return sqLiteDatabase.delete(MY_DATABASE_TABLE2,"id=?",new String[]{String.valueOf(id)});
    }


    //to insert user into the table

    public long insertUser(String username, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        return sqLiteDatabase.insert(MY_DATABASE_TABLE,null,contentValues);
    }

    public long insertList (int id,String website, String password,String name ){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put(KEY_CONTENT,website);
        contentValues.put(KEY_CONTENT_1,name);
        contentValues.put(KEY_CONTENT_2,password);


        return sqLiteDatabase.insert(MY_DATABASE_TABLE2,null,contentValues);
    }

    //SQLiteOpenHelper: A helper class to manage the database creation and version management

    public class SQLiteHelper extends SQLiteOpenHelper{

        //4 parameters constructor
        public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
            super(context,name,factory,version);
        }

        //Create the database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase){
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE2);
        }

        //manage the version
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,int i,int i1){

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MY_DATABASE_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MY_DATABASE_TABLE2);
            onCreate(sqLiteDatabase);

        }
    }
}
