package edu.uda.pets.BD;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import edu.uda.pets.Interfaz.IF_Badge_Infor;
import edu.uda.pets.POJO.RecentPost;

public class ConstructorFavoritos {

    private Context context;
    private IF_Badge_Infor if_badge_infor;
    public ConstructorFavoritos(IF_Badge_Infor if_badge_infor, Context context) {
        this.if_badge_infor = if_badge_infor;
        this.context = context;
        getSizieFavoritesInit();
    }

    private void getSizieFavoritesInit() {
        ArrayList<RecentPost> listCountFavorites = ObtenerPetFavorito();
        if_badge_infor.numberFavorites(listCountFavorites.size());
    }

    public ConstructorFavoritos(Context context) {
        this.context = context;
    }

    public void insertarFavorito(BaseDatosFavoritos dbF, RecentPost dataPet){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ConstantesBD.NAME_PET, dataPet.getName());
        contentValues.put(ConstantesBD.BREED, dataPet.getBreed());
        contentValues.put(ConstantesBD.LIKES, dataPet.getLikes());
        contentValues.put(ConstantesBD.PIC_POSTED, dataPet.getRecentPostPic());
        contentValues.put(ConstantesBD.PIC_PROFILE, dataPet.getProfilePic());
        dbF.insertarFavorito(contentValues);

        ArrayList<RecentPost> listCountFavorites = ObtenerPetFavorito();
        if_badge_infor.numberFavorites(listCountFavorites.size());

    }
    public void BorrarFilaFavorito(BaseDatosFavoritos dbF, int code){
        dbF.borrarFila(code);
    }

    public ArrayList<RecentPost> ObtenerPetFavorito(){
        BaseDatosFavoritos dbFavoritos = new BaseDatosFavoritos(context);
        return dbFavoritos.obtenerFavoritos();
    }
}
