package com.example.fa_sagarsaini_c0827838_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME="locationDb";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="favlocations";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String LAT="lati";
    public static final String LONG="longi";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql=
        "CREATE TABLE IF NOT EXISTS favlocations("+
                "id INTEGER NOT NULL CONSTRAINT pk PRIMARY KEY AUTOINCREMENT,"+
                "name VARCHAR(20) NOT NULL,"+
                "lati DOUBLE,"+
                "longi DOUBLE NOT NULL);";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }
    void additem(String name,double lati, double longi)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(NAME,name);
        cv.put(LAT,lati);
        cv.put(LONG,longi);
        long result = db.insert(TABLE_NAME,null,cv);
        if (result == -1)
        {
            Toast.makeText(context, "Adding Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Place Added In Favourite List ", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readdata()
    {
    String sql="SELECT * FROM favlocations";
    SQLiteDatabase db= this.getReadableDatabase();
    Cursor cursor=null;
    if(db!=null)
    {
    cursor = db.rawQuery(sql,null);
    }
    return cursor;
    }

    void updateData(String id1,String name, double lati, double longi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(NAME,name);
        cv.put(LAT,lati);
        cv.put(LONG,longi);
        long result = db.update("favlocations",cv,"id=?",new String[]{id1});
        if (result == -1)
        {
            Toast.makeText(context, "Updation Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }

    }
    void ondelete(String id1)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result =db.delete(TABLE_NAME,"id=?",new String[]{id1});
        if (result == -1)
        {
            Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
