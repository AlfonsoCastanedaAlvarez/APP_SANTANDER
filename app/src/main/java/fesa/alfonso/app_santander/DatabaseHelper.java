package fesa.alfonso.app_santander;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Banco.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "apellido TEXT, " +
                "correo TEXT, " +
                "telefono TEXT, " +
                "password TEXT, " +
                "saldo REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertarUsuario(String nombre, String apellido, String correo, String telefono, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellido", apellido);
        values.put("correo", correo);
        values.put("telefono", telefono);
        values.put("password", password);
        values.put("saldo", 100000.0);

        long resultado = db.insert("usuarios", null, values);
        return resultado != -1;
    }

    public boolean validarUsuario(String correo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE correo=? AND password=?",
                new String[]{correo, password}
        );

        boolean existe = cursor.getCount() > 0;
        cursor.close();

        return existe;
    }

    public boolean correoExiste(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE correo=?",
                new String[]{correo}
        );

        boolean existe = cursor.getCount() > 0;
        cursor.close();

        return existe;
    }

    public double obtenerSaldo(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT saldo FROM usuarios WHERE correo=?",
                new String[]{correo}
        );

        double saldo = 0;

        if (cursor.moveToFirst()) {
            saldo = cursor.getDouble(0);
        }

        cursor.close();
        return saldo;
    }

    public boolean transferir(String correoOrigen, String correoDestino, double monto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        Cursor c1 = null;
        Cursor c2 = null;

        try {
            if (correoOrigen.equals(correoDestino)) return false;

            c1 = db.rawQuery(
                    "SELECT saldo FROM usuarios WHERE correo=?",
                    new String[]{correoOrigen}
            );

            if (!c1.moveToFirst()) return false;

            double saldoOrigen = c1.getDouble(0);
            if (saldoOrigen < monto) return false;

            c2 = db.rawQuery(
                    "SELECT saldo FROM usuarios WHERE correo=?",
                    new String[]{correoDestino}
            );

            if (!c2.moveToFirst()) return false;

            double saldoDestino = c2.getDouble(0);

            ContentValues v1 = new ContentValues();
            v1.put("saldo", saldoOrigen - monto);
            db.update("usuarios", v1, "correo=?", new String[]{correoOrigen});

            ContentValues v2 = new ContentValues();
            v2.put("saldo", saldoDestino + monto);
            db.update("usuarios", v2, "correo=?", new String[]{correoDestino});

            db.setTransactionSuccessful();
            return true;

        } finally {
            if (c1 != null) c1.close();
            if (c2 != null) c2.close();
            db.endTransaction();
        }
    }
}