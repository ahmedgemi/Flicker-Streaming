package codes.platform.flickerstreaming.DataStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import codes.platform.flickerstreaming.Image;

/**
 * Created by ahmed on 22/03/17.
 */

public class ImageDatabase extends SQLiteOpenHelper {

    /*Database attribute names*/
    private final static  String database_name = "ImageDatabase";
    private final static  String table_name = "ImageInfo";
    private final static  String key_id = "id";
    private final static  String key_title ="title";
    private final static  String key_path = "path";



    public ImageDatabase(Context context) {

        super(context,database_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        /*Database creation query*/
        String query = "CREATE TABLE " + table_name + "(" +  key_id + " TEXT PRIMARY KEY," +  key_title + " TEXT," +  key_path + " TEXT)";

        db.execSQL(query);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //update table
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }


    public void insert (Image image){ /* insert into database*/


        ContentValues contentValues = new ContentValues();

        contentValues.put(key_id , image.getID() );
        contentValues.put(key_title, image.getTitle() );
        contentValues.put(key_path,image.getPath() );

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(table_name ,null,contentValues);
    }


    public boolean isExist(String id){ /*check if image ID already exist ?*/

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT "+ key_id + " FROM " + table_name + " WHERE " + key_id + " = " + "\'" + id + "\'";

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst())
            return true;
        else
            return false;

    }



    public ArrayList<Image> getData(){ /* retrieve all data form cache*/

        ArrayList<Image> list = new ArrayList<Image>();


        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + table_name ;

        Cursor cursor = db.rawQuery(query,null);



        if (cursor.moveToFirst()) {


            do {


                String id = cursor.getString(0);
                String title = cursor.getString(1);
                String path = cursor.getString(2);


                list.add(new Image(id, title, path));



            } while (cursor.moveToNext());


        }


        return list ;
    }



}
