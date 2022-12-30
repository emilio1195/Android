package edu.uda.pets.Fragmentos;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uda.pets.Adapter.AdaptadorListaPets;
import edu.uda.pets.Interfaz.IF_Badge_Infor;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.R;
import edu.uda.pets.utils.FilesOperations;


public class Home_Fragment extends Fragment implements IF_Badge_Infor {

    ArrayList<RecentPost> ListaPets;
    private RecyclerView recyclerView;


    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_fragment_recycler_view, container, false);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.RVPet);
        LinearLayoutManager linearLayoutV = new LinearLayoutManager(getActivity()); //Se colocara en este contexto un LinearLayout Vertical
        linearLayoutV.setOrientation(RecyclerView.VERTICAL);
        // que formara el Recycler view en la actividad
        recyclerView.setLayoutManager(linearLayoutV); //configuramos que el recycler view obtenga todas las caracteristicas de un LinearLayout
        getRecentPostsPets();
        CrearAdapter();
        return v;
    }

    private void getRecentPostsPets(){
        Gson gson = new Gson();
        String jsonContent = null;
        ListaPets = new ArrayList<RecentPost>();
        try{
            jsonContent = FilesOperations.readFile(getActivity(), "recent_posts.json");
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONArray jsonArray = jsonObject.getJSONArray("posts");

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonPet = jsonArray.getJSONObject(i);
                String stringJsonPet = jsonPet.toString();
                RecentPost recentPost = gson.fromJson(stringJsonPet, RecentPost.class);
                ListaPets.add(recentPost);
            }
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }

        /*ListaPets.add(new DataPet("Lita","Chiguagua","201", R.drawable.lita,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Valentín","Chiguagua","501",R.drawable.vale,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Ojis","Balinese","744",R.drawable.ojis,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Kata","Angora Turca","265",R.drawable.kata,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Kyra","Schnauzer","911",R.drawable.kyra,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Mia","American Wirehair","311",R.drawable.mia,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Chily","French Poodle","881",R.drawable.chily,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Franshua","Scottish Fold","981",R.drawable.munchkin,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Genna","Blood Hound","571",R.drawable.genna,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Max","Pastor Alemán","651",R.drawable.nena,false,R.drawable.unlike));
        ListaPets.add(new DataPet("Nory","Scottish Fold","981",R.drawable.scottish_fold,false,R.drawable.unlike));
*/    }

    private void CrearAdapter(){
        AdaptadorListaPets adaptadorListaPets = new AdaptadorListaPets(ListaPets, getActivity(), this);
        recyclerView.setAdapter(adaptadorListaPets);
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void numberFavorites(int sizeFavorites) {
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this.getContext());
        badgeDrawable.setNumber(sizeFavorites);
        BadgeUtils.attachBadgeDrawable(badgeDrawable, getActivity().findViewById(R.id.toolbar), R.id.btn_favoritos);

    }

    @Override
    public void numberMessages(int numMessages) {

    }
}
