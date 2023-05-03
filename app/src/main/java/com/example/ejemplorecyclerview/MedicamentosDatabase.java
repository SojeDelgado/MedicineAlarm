package com.example.ejemplorecyclerview;

import static com.example.ejemplorecyclerview.MedicamentosDatabase.MedicamentosEntry.COLUMN_CANTIDAD;
import static com.example.ejemplorecyclerview.MedicamentosDatabase.MedicamentosEntry.COLUMN_NOMBRE;
import static com.example.ejemplorecyclerview.MedicamentosDatabase.MedicamentosEntry.TABLE_NAME;
import static com.example.ejemplorecyclerview.MedicamentosDatabase.MedicamentosEntry._ID;

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
                + COLUMN_NOMBRE + " TEXT NOT NULL, "
                + MedicamentosEntry.COLUMN_CANTIDAD + " REAL NOT NULL, "
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

    public long insertarMedicamento(String color, String nombre, float cantidad,String medicamento, Date horaToma, Boolean onoff) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedicamentosEntry.COLUMN_COLOR, color);
        values.put(COLUMN_NOMBRE, nombre);
        values.put(MedicamentosEntry.COLUMN_CANTIDAD, cantidad);
        values.put(MedicamentosEntry.COLUMN_MEDICAMENTO, medicamento);
        values.put(MedicamentosEntry.COLUMN_HORA_TOMA, horaToma.getTime());
        values.put(MedicamentosEntry.COLUMN_ON_OFF, onoff);

        values.put(MedicamentosEntry._ID, getNextId());

        long id = db.insert(MedicamentosEntry.TABLE_NAME, null, values);
        db.close();

        return id;
    }
    public int getNextId() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT MAX(" + MedicamentosEntry._ID + ") FROM " + MedicamentosEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int maxId = 0;
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0);
        }
        cursor.close();
        return maxId + 1;
    }


    public List<MedicamentoElement> obtenerMedicamentos() {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                MedicamentosEntry._ID,
                MedicamentosEntry.COLUMN_COLOR,
                COLUMN_NOMBRE,
                MedicamentosEntry.COLUMN_CANTIDAD,
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
            int id = (int) cursor.getLong(cursor.getColumnIndex(MedicamentosEntry._ID));
            String color = cursor.getString(cursor.getColumnIndex(MedicamentosEntry.COLUMN_COLOR));
            String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE));
            float cantidad = cursor.getFloat(cursor.getColumnIndex(MedicamentosEntry.COLUMN_CANTIDAD));
            String medicamento = cursor.getString(cursor.getColumnIndex(MedicamentosEntry.COLUMN_MEDICAMENTO));
            long horaTomaMillis = cursor.getLong(cursor.getColumnIndex(MedicamentosEntry.COLUMN_HORA_TOMA));
            boolean onOff = (cursor.getInt(cursor.getColumnIndex(MedicamentosEntry.COLUMN_ON_OFF)) != 0);

            Date hora = new Date(horaTomaMillis);

            MedicamentoElement medi = new MedicamentoElement(id,color,nombre, cantidad,medicamento, hora, onOff);
            medicamentos.add(medi);
        }

        cursor.close();
        db.close();

        return medicamentos;
    }
    public void actualizarMedicamento(int id, String color, String nombre, float cantidad,String medicamento, Date hora, boolean activo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MedicamentosEntry.COLUMN_COLOR, color);
        values.put(COLUMN_NOMBRE, nombre);
        values.put(MedicamentosEntry.COLUMN_CANTIDAD, cantidad);
        values.put(MedicamentosEntry.COLUMN_MEDICAMENTO, medicamento);
        values.put(MedicamentosEntry.COLUMN_HORA_TOMA, hora.getTime());
        values.put(MedicamentosEntry.COLUMN_ON_OFF, activo ? 1 : 0);
        db.update("medicamentos", values, "_id = ?", new String[]{Integer.toString(id)});
        db.close();
    }


    public void eliminarMedicamento(MedicamentoElement medicamento) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("medicamentos", "_id = ?", new String[] { Integer.toString(medicamento.getId()) });
        Log.d(TAG, "Medicamento eliminado: " + medicamento.getNombre());
        db.close();
    }

    public String getMedicineName(int alarmId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_NOMBRE};
        String selection = _ID + "=?";
        String[] selectionArgs = {String.valueOf(alarmId)};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String medicineName = null;
        if (cursor.moveToFirst()) {
            medicineName = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE));
        }
        cursor.close();
        Log.d("MyDatabaseHelper", "getMedicineName: " + medicineName);
        return medicineName;
    }



    public static class MedicamentosEntry implements BaseColumns {
        public static final String TABLE_NAME = "medicamentos";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_CANTIDAD = "cantidad";
        public static final String COLUMN_MEDICAMENTO = "medicamento";
        public static final String COLUMN_HORA_TOMA = "hora_toma";
        public static final String COLUMN_ON_OFF = "onoff";
    }
}