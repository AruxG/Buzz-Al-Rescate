package com.example.buzzalrescate.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {

    private DbHelper helper;
    private SQLiteDatabase db;

    public DbManager(Context context) {
        this.helper = new DbHelper(context);
        this.db = this.helper.getWritableDatabase();
    }

    public void insertEntry(String nombre, int puntos){
        this.db.insert(DbContract.DbEntry.TABLE_NAME_RANKING, null,
                this.generateContentValues(nombre,puntos));
    }

    public Cursor getEntries (String table) {
        String[] columns = new String[]{
                DbContract.DbEntry.COLUMN_NAME_PLAYER,
                DbContract.DbEntry.COLUMN_NAME_MAX,
        };
        return db.query(table, columns, null, null,
                null, null, "maximapuntuacion DESC");

    }

    public boolean tablaVacia(String table){
        Cursor cursor = getEntries(table);
        if(cursor != null && cursor.getCount() > 0){
            return false;
        }else{
            return true;
        }
    }

    public void deleteAll() {
        this.db.delete(DbContract.DbEntry.TABLE_NAME_RANKING, null,null);
    }

    private ContentValues generateContentValues(String nombre, int puntos){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.DbEntry.COLUMN_NAME_PLAYER, nombre);
        contentValues.put(DbContract.DbEntry.COLUMN_NAME_MAX, puntos);
        return contentValues;
    }

    public boolean deleteTitle(String table, String nombre, int puntuacion)
    {
        return this.db.delete(table, "player='" + nombre+ "' AND maximapuntuacion ="+puntuacion, null) > 0;
    }

}
