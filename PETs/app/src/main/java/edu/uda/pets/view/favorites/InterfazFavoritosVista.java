package edu.uda.pets.view.favorites;

import java.util.ArrayList;

import edu.uda.pets.Adapter.AdaptadorFavoritos;
import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.POJO.RecentPost;

public interface InterfazFavoritosVista {
    public void generarLayoutRCV();
    public AdaptadorFavoritos crearAdaptador(ArrayList<RecentPost> datosPet);
    public void InitAdaptadorfav (AdaptadorFavoritos adaptadorFavoritos);
}
