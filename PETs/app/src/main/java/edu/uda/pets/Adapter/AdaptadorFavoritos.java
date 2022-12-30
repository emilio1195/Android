package edu.uda.pets.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import edu.uda.pets.Interfaz.IF_Badge_Infor;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.R;

//se realiza una extension de la misma clase con el objetivo de lograr el reciclaje
public class AdaptadorFavoritos extends RecyclerView.Adapter<AdaptadorFavoritos.PetsViewHolder> {
    private final Activity activity;
    ArrayList<RecentPost> ListaPets;

    public AdaptadorFavoritos(ArrayList<RecentPost> listaPets, Activity activity) {
        ListaPets = listaPets;
        this.activity = activity;
    }

    @Override
    public PetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se encargara de crear la Vista y retornarla
        //Se utilizara el Inflate, el cual se encarga de construir el layout, es decir ir inflando nuestras vistas
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pet,parent,false);
        return new PetsViewHolder(vista); //se retorna este objeto con el fin de poder colocar el parametro y usar las vistas.
    }

    @Override
    public void onBindViewHolder(@NonNull final PetsViewHolder datoPetViewHolder, int position) {
        // Asocia las lista de datos con cada view y en nuestro ViewHolder
        final RecentPost datosPetFavorite = ListaPets.get(position); //guardamos la posicion de la lista en el obj tipo DatosPets
        //en el ViewHolder tenemos inicializada las vistas por lo cual, seteamos sus valores en este metodo, obteniendo del listado datos
        String imageProfileName = datosPetFavorite.getRecentPostPic();
        int imageResourceProfile = activity.getResources().getIdentifier(imageProfileName, "drawable", activity.getPackageName());
        String imagePostedName = datosPetFavorite.getRecentPostPic();
        int imageResourcePosted = activity.getResources().getIdentifier(imagePostedName, "drawable", activity.getPackageName());

        datoPetViewHolder.img_profile.setImageResource(imageResourceProfile);
        datoPetViewHolder.fotoPet.setImageResource(imageResourcePosted);
        datoPetViewHolder.nombrePet.setText(datosPetFavorite.getName());
        datoPetViewHolder.raza.setText(datosPetFavorite.getBreed());
        datoPetViewHolder.contadorLikes.setText(datosPetFavorite.getLikes() + " LIKES");

        datoPetViewHolder.StateLike.setImageResource(R.drawable.like);

        // Toast.makeText(activity,"te Gusta: " + datosPets.getNombre() + ", N likes: " + datosPets.getCountLikes() + ", state Like:" + datosPets.isLike(),Toast.LENGTH_LONG).show();

        datoPetViewHolder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (datoPetViewHolder.container_comment.getVisibility()){
                    case View.GONE:
                        datoPetViewHolder.container_comment.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        datoPetViewHolder.container_comment.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //nos entrega la cantida de item de nuestra lista que se manejaran
        return ListaPets.size();
    }

    public static class PetsViewHolder extends RecyclerView.ViewHolder{

        TextView nombrePet, raza, contadorLikes;
        ImageView fotoPet, StateLike, btn_comment, btn_shared;
        CircularImageView img_profile;
        LinearLayout container_comment;

        public PetsViewHolder(@NonNull View itemView) {
            super(itemView);
            img_profile = (CircularImageView) itemView.findViewById(R.id.img_profile);
            nombrePet =(TextView) itemView.findViewById(R.id.nombre);
            raza =(TextView) itemView.findViewById(R.id.raza);
            contadorLikes =(TextView) itemView.findViewById(R.id.contadorLikes);
            fotoPet =(ImageView) itemView.findViewById(R.id.img_posted_profile);
            StateLike = (ImageView) itemView.findViewById(R.id.like);
            btn_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            container_comment = (LinearLayout) itemView.findViewById(R.id.container_comment);

            container_comment.setVisibility(View.GONE);
        }
    }
}
