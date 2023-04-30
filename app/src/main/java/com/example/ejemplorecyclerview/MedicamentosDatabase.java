package com.example.ejemplorecyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicamentosDatabase extends SQLiteOpenHelper {
    private static final String TAG = MedicamentosDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "medicamentos.db";
    private static final int DATABASE_VERSION = 1;

    public MedicamentosDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MEDICAMENTOS_TABLE = "CREATE TABLE " + MedicamentosEntry.TABLE_NAME + " ("
                + MedicamentosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicamentosEntry.COLUMN_COLOR + " VARCHAR2(7) NOT NULL, "
                + MedicamentosEntry.COLUMN_NOMBRE + " TEXT NOT NULL, "
                + MedicamentosEntry.COLUMN_MEDICAMENTO + " TEXT NOT NULL, "
                + MedicamentosEntry.COLUMN_HORA_TOMA + " INTEGER NOT NULL,"
                + MedicamentosEntry.COLUMN_ON_OFF + " BOOLEAN NOT NULL);";

        db.execSQL(SQL_CREATE_MEDICAMENTOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Actualizando la base de datos de la versión " + oldVersion + " a la versión " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + MedicamentosEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insertarMedicamento(String color, String nombre, String medicamento, Date horaToma, Boolean onoff) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedicamentosEntry.COLUMN_COLOR, color);
        values.put(MedicamentosEntry.COLUMN_NOMBRE, nombre);
        values.put(MedicamentosEntry.COLUMN_MEDICAMENTO, medicamento);
        values.put(MedicamentosEntry.COLUMN_HORA_TOMA, horaToma.getTime());
        values.put(MedicamentosEntry.COLUMN_ON_OFF, onoff);

        long id = db.insert(MedicamentosEntry.TABLE_NAME, null, values);
        db.close();

        return id;
    }


    public List<MedicamentoElement> obtenerMedicamentos() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                MedicamentosEntry._ID,
                MedicamentosEntry.COLUMN_COLOR,
                MedicamentosEntry.COLUMN_NOMBRE,
                MedicamentosEntry.COLUMN_MEDICAMENTO,
                MedicamentosEntry.COLUMN_HORA_TOMA,
                MedicamentosEntry.COLUMN_ON_OFF
        };


        Cursor cursor = db.query(
                MedicamentosEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<MedicamentoElement> medicamentos = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(MedicamentosEntry._ID));
            String color = cursor.getString(cursor.getColumnIndex(MedicamentosEntry.COLUMN_COLOR));
            String nombre = cursor.getString(cursor.getColumnIndex(MedicamentosEntry.COLUMN_NOMBRE));
            String medicamento = cursor.getString(cursor.getColumnIndex(MedicamentosEntry.COLUMN_MEDICAMENTO));
            long horaTomaMillis = cursor.getLong(cursor.getColumnIndex(MedicamentosEntry.COLUMN_HORA_TOMA));
            boolean onOff = (cursor.getInt(cursor.getColumnIndex(MedicamentosEntry.COLUMN_ON_OFF)) != 0);

            Date hora = new Date(horaTomaMillis);

            MedicamentoElement medi = new MedicamentoElement(color,nombre, medicamento, hora, onOff);
            medicamentos.add(medi);
        }

        cursor.close();
        db.close();

        return medicamentos;
    }

    public long eliminarMedicamento(long id) {
        SQLiteDatabase db = getWritableDatabase();
        long eliminados = db.delete(MedicamentosEntry.TABLE_NAME, MedicamentosEntry._ID + "=?", new String[] { String.valueOf(id) });
        db.close();
        return eliminados;
    }

    public static class MedicamentosEntry implements BaseColumns {
        public static final String TABLE_NAME = "medicamentos";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_MEDICAMENTO = "medicamento";
        public static final String COLUMN_HORA_TOMA = "hora_toma";
        public static final String COLUMN_ON_OFF = "onoff";
    }
}
