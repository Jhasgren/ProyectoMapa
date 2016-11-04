package com.example.proyectomapa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ing_l on 03/11/2016.
 */

public class EnlaceBD {
    private static EnlaceBD enlace;
    private SQLiteDatabase bd;

    public EnlaceBD(Context context) {
        SQLiteOpenHelper sql=new SQLiteOpenHelper(context, "principal", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists usuarios(" +
                        "nombre text, usuario text, clave text)");
                db.execSQL("insert into usuarios values('Fernanda', 'fer', '1234')");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        bd=sql.getWritableDatabase();
    }

    public void comando(String sql){
        bd.execSQL(sql);
    }

    public Cursor consulta(String sql){
        return bd.rawQuery(sql, null);
    }

    public static EnlaceBD getEnlace() {
        return enlace;
    }
}
