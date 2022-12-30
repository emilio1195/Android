package edu.uda.pets.Presentador.favorites;

import android.content.Context;

import java.util.ArrayList;

import edu.uda.pets.BD.ConstructorFavoritos;
import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.view.favorites.InterfazFavoritosVista;

public class FavoritosPetsPresentador implements InterfazFavoritosPresentador {

    private InterfazFavoritosVista interfazFavoritosVista;
    private Context context;
    private ConstructorFavoritos constructorFavoritos;
    private ArrayList<RecentPost> datosPets;

    public FavoritosPetsPresentador(InterfazFavoritosVista interfazFavoritosVista, Context context) {
        this.interfazFavoritosVista = interfazFavoritosVista;
        this.context = context;
        obtenerPetsFavoritos();
    }

    @Override
    public void obtenerPetsFavoritos() {
        constructorFavoritos = new ConstructorFavoritos(context);
        datosPets = constructorFavoritos.ObtenerPetFavorito();
        mostrarPetsfavoritos();
    }

    @Override
    public void mostrarPetsfavoritos() {
        interfazFavoritosVista.InitAdaptadorfav(interfazFavoritosVista.crearAdaptador(datosPets));
        interfazFavoritosVista.generarLayoutRCV();
    }
}
