package edu.uda.pets.view.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import edu.uda.pets.Adapter.AdaptadorFavoritos;
import edu.uda.pets.POJO.DataPet;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.Presentador.favorites.FavoritosPetsPresentador;
import edu.uda.pets.Presentador.favorites.InterfazFavoritosPresentador;
import edu.uda.pets.R;

public class FavoritosPets extends AppCompatActivity implements InterfazFavoritosVista {
    private ArrayList<DataPet> pilaPets;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Context context;
    private InterfazFavoritosPresentador interfazFavoritosPresentador;
    ArrayList<DataPet> lista = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos_pets);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null ){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); //se puede activar aqui o atraves del manifest
        recyclerView = (RecyclerView) findViewById(R.id.RVFavoritos);

        ////Se instancia el obj de presentador el cual inicializara todo los metodos que van en cadena
        // para ejecutar la extraccion de los dabos de la BD
        interfazFavoritosPresentador  = new FavoritosPetsPresentador(this,getBaseContext());
    }

    @Override
    public void generarLayoutRCV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public AdaptadorFavoritos crearAdaptador(ArrayList<RecentPost> datosPet) {
        AdaptadorFavoritos adapterFavoritos = new AdaptadorFavoritos(datosPet,this);
        return adapterFavoritos;
    }

    @Override
    public void InitAdaptadorfav(AdaptadorFavoritos adaptadorFavoritos) {
        recyclerView.setAdapter(adaptadorFavoritos);
    }
}
