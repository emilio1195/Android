package edu.uda.base_de_datos_completa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter
{
    static final String KEY_CODIGO = "Codigo";
    static final String KEY_NOMBRE = "Nombre";
    static final String KEY_GENERO = "Genero";
    static final String KEY_AÑO = "Anio";
    static final String KEY_TELEF = "Telefono";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "BandasMusica";
    static final String DATABASE_TABLE = "bandas";
    static final int DATABASE_VERSION = 1;
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try
            {
//db.execSQL(DATABASE_CREATE);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                    newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacto");
            onCreate(db);
        }
    }
    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        DBHelper.close();
    }
    // ingresa contactos
    public long insertContact(String code, String name, String genre, String year, String telef)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODIGO, code);
        initialValues.put(KEY_NOMBRE, name);
        initialValues.put(KEY_GENERO, genre);
        initialValues.put(KEY_AÑO, year);
        initialValues.put(KEY_TELEF, telef);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    // borra dato con el code
    public boolean deleteContact(long rowCode)
    {
        return db.delete(DATABASE_TABLE, KEY_CODIGO + "=" + rowCode, null) > 0;
    }

    // borra dato con el telef
    public boolean deleteDatoTelef(long telef)
    {
        return db.delete(DATABASE_TABLE, KEY_TELEF + "=" + telef, null) > 0;
    }

    // recupera los contactos
    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_CODIGO, KEY_NOMBRE,
                KEY_GENERO, KEY_AÑO, KEY_TELEF}, null, null, null, null, null);
    }
    // consulta un contacto con el code
    public Cursor getContact(long rowCode) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_CODIGO,
                KEY_NOMBRE, KEY_GENERO, KEY_AÑO, KEY_TELEF}, KEY_CODIGO + "=" + rowCode, null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // consulta un dato con el telefono
    public Cursor getDatoTelef(long telef) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_CODIGO,
                KEY_NOMBRE, KEY_GENERO, KEY_AÑO, KEY_TELEF}, KEY_TELEF + "=" + telef, null, null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToLast();
        }
        return mCursor;
    }

    // actualiza un contacto
    public boolean updateContact(long code, String name, String genre, String year, String telef)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NOMBRE, name);
        args.put(KEY_GENERO, genre);
        args.put(KEY_AÑO, year);
        args.put(KEY_TELEF, telef);
        return db.update(DATABASE_TABLE, args, KEY_CODIGO + "=" + code, null) > 0;
    }
}