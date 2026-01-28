package com.example.ecocity_efm_drm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Clase encargada de manejar la base de datos SQLite

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecocity.db"; // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1; // Versión de la base de datos

    public static final String TABLE_INCIDENCIAS = "incidencias"; // Tabla de incidencias

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creara la tabla donde se guardarán todas las incidencias

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE incidencias (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT," +
                        "descripcion TEXT," +
                        "urgencia TEXT," +
                        "latitud REAL," +
                        "longitud REAL," +
                        "fecha INTEGER)"
        );

    }

    // Se ejecutará si la versión de la base de datos cambia

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS incidencias");
        onCreate(db);

    }

    // -- PARTE DEL CRUD

    // Insertar una nueva incidencia

    public void insertar(String titulo, String descripcion, String urgencia, double lat, double lon, long fecha) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("urgencia", urgencia);
        values.put("latitud", lat);
        values.put("longitud", lon);
        values.put("fecha", fecha);

        db.insert(TABLE_INCIDENCIAS, null, values);
        db.close(); // Cerramos la base de datos para liberar recursos

    }

    // Obtener todas las incidencias

    public Cursor obtenerTodas() {

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM incidencias ORDER BY fecha DESC", null);

    }

    // Actualizar una incidencia existente

    public boolean actualizar(int id, String titulo, String descripcion, String urgencia) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("urgencia", urgencia);

        int filas = db.update(TABLE_INCIDENCIAS, values, "id=?", new String[]{String.valueOf(id)});

        db.close();

        return filas > 0; // true si se actualizó alguna fila

    }

    // Borrar una incidencia

    public boolean borrar(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int filas = db.delete(TABLE_INCIDENCIAS, "id=?", new String[]{String.valueOf(id)}); // Encontrará la incidencia por ID

        db.close(); // Cerrara la conexión con la base de datos para liberar recursos

        return filas > 0; // true si se borró algo
    }

}
