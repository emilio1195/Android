package edu.uda.pets.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.uda.pets.POJO.RecentPost;

public class BaseDatosFavoritos extends SQLiteOpenHelper {
    Context context;
    public BaseDatosFavoritos(@Nullable Context context){
        super(context, ConstantesBD.DATABASE_NAME, null, ConstantesBD.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCrearTabla = "CREATE TABLE " + ConstantesBD.TABLE_PETS + "(" +
                ConstantesBD.CODE_PET + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConstantesBD.NAME_PET + " TEXT, " +
                ConstantesBD.BREED + " TEXT, " +
                ConstantesBD.LIKES + " TEXT, " +
                ConstantesBD.PIC_POSTED + " TEXT, " +
                ConstantesBD.PIC_PROFILE + " TEXT" + ")";

        db.execSQL(queryCrearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String condicion = "DROP TABLE IF EXIST";
        db.execSQL(condicion + ConstantesBD.TABLE_PETS);
        onCreate(db);
    }

    public ArrayList<RecentPost> obtenerFavoritos(){
        ArrayList<RecentPost> datosPets = new ArrayList<>();
        long CANTIDAD_FAVORITOS = 5;
        String querySelect = "SELECT * FROM " + ConstantesBD.TABLE_PETS + " LIMIT " + (NumFilas() - CANTIDAD_FAVORITOS) +","+ NumFilas();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(querySelect,null);
        while (cursor.moveToNext()){
            RecentPost favoritoActual = new RecentPost();
            favoritoActual.setName(cursor.getString(1));
            favoritoActual.setBreed(cursor.getString(2));
            favoritoActual.setLikes(cursor.getString(3));
            favoritoActual.setRecentPostPic(cursor.getString(4));
            favoritoActual.setProfilePic(cursor.getString(5));
            ///*******************************
            //en este apartado precargo los otros datos que no son relevantes que fueron utilizados
            //para colocar el estado de like la imagen respectiva del like
            //Estos datos no son importantes ni necesarios para la BD, por ello los precargo directamente
            favoritoActual.setLike(true);
            ////////////////////////////////////////////////////////////////////////////////////

            datosPets.add(favoritoActual);
        }
        db.close();
        return  datosPets;
    }

    public void insertarFavorito(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBD.TABLE_PETS, null, contentValues); //Esto es como una Actualizacion del guardado de datos en la tabla
        db.close();
    }

    public void borrarFila(int code){
        String queryEliminarFila = "DELETE FROM "+ ConstantesBD.TABLE_PETS +" WHERE " + ConstantesBD.CODE_PET +"=" + code;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryEliminarFila);
        db.close();
    }

    public long NumFilas()   ///Consulta el numero de filas en nuestra tabla
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, ConstantesBD.TABLE_PETS);
    }
}
