package edu.uda.pets.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import edu.uda.pets.BD.BaseDatosFavoritos;
import edu.uda.pets.BD.ConstructorFavoritos;
import edu.uda.pets.ChatWindow;
import edu.uda.pets.Interfaz.IF_Badge_Infor;
import edu.uda.pets.POJO.RecentPost;
import edu.uda.pets.ProfileView;
import edu.uda.pets.R;

//se realiza una extension de la misma clase con el objetivo de lograr el reciclaje
public class AdaptadorListaPets extends RecyclerView.Adapter<AdaptadorListaPets.PetsViewHolder> {
    private final Activity activity;
    private IF_Badge_Infor if_badge_infor;
    ArrayList<RecentPost> ListaPets;
    BaseDatosFavoritos dbF;
    ConstructorFavoritos constructorFavoritos;

    public AdaptadorListaPets(ArrayList<RecentPost> listaPets, Activity activity, IF_Badge_Infor if_badge_infor) {
        ListaPets = listaPets;
        this.activity = activity;
        this.if_badge_infor = if_badge_infor;
        constructorFavoritos = new ConstructorFavoritos(if_badge_infor, activity);
        //String queryNumFilas= "SELECT COUNT(*) FROM" + ConstantesBD.TABLE_PETS;
    }

    @Override
    public PetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se encargara de crear la Vista y retornarla
        //Se utilizara el Inflate, el cual se encarga de construir el layout, es decir ir inflando nuestras vistas
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pet,parent,false);
        dbF= new BaseDatosFavoritos(activity);
        return new PetsViewHolder(vista); //se retorna este objeto con el fin de poder colocar el parametro y usar las vistas.
    }

    @Override
    public void onBindViewHolder(@NonNull final PetsViewHolder datoPetViewHolder, int position) {
        // Asocia las lista de datos con cada view y en nuestro ViewHolder
        final RecentPost datosPet_Post = ListaPets.get(position); //guardamos la posicion de la lista en el obj tipo DatosPets
        //en el ViewHolder tenemos inicializada las vistas por lo cual, seteamos sus valores en este metodo, obteniendo del listado datos
        String imageProfileName = datosPet_Post.getProfilePic();
        int imageResourceProfile = activity.getResources().getIdentifier(imageProfileName, "drawable", activity.getPackageName());
        String imagePostedName = datosPet_Post.getRecentPostPic();
        int imageResourcePosted = activity.getResources().getIdentifier(imagePostedName, "drawable", activity.getPackageName());

        datoPetViewHolder.img_profile.setImageResource(imageResourceProfile);
        datoPetViewHolder.RecentPostPic.setImageResource(imageResourcePosted);
        //datoPetViewHolder.RecentPostPic.setScaleType(ImageView.ScaleType.FIT_CENTER);
        datoPetViewHolder.namePet.setText(datosPet_Post.getName());
        datoPetViewHolder.breed.setText(datosPet_Post.getBreed());
        datoPetViewHolder.numberLikes.setText(datosPet_Post.getLikes()  + " LIKES");

        datoPetViewHolder.StateLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int conteo;
                if (datosPet_Post.getLike()){        ///condicional para evaluar si se da like o si se quita el like
                    datosPet_Post.setLike(false);       //obj que contiene el estado del like
                    conteo = Integer.parseInt(datosPet_Post.getLikes()) - 1;   // se resta un decimal al quitar el like
                    datoPetViewHolder.StateLike.setImageResource(R.drawable.unlike);       ///se actualiza la ImagenLike con el estado actual
                }
                else {  //condicional cuando le da a me gusta
                    datosPet_Post.setLike(true);
                    datoPetViewHolder.StateLike.setImageResource(R.drawable.like);       ///se actualiza la ImagenLike con el estado actual
                    conteo = Integer.parseInt(datosPet_Post.getLikes()) + 1;
                    datosPet_Post.setLikes(String.valueOf(conteo)); //se guarda el conteo en el obj

                    //Toast.makeText(activity,"numFav: " + numFavoritos,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(activity,"numPulsos: " + numPulsos,Toast.LENGTH_SHORT).show();
                    constructorFavoritos.insertarFavorito(dbF, datosPet_Post);
                }
                datosPet_Post.setLikes(String.valueOf(conteo)); //se guarda el conteo en el obj
                datoPetViewHolder.numberLikes.setText(datosPet_Post.getLikes()  + " LIKES");         //Se actualiza el TextView contador al dar like o quitarlo
               // Toast.makeText(activity,"te Gusta: " + datosPet_Post.getNombre() + ", N likes: " + datosPet_Post.getCountLikes() + ", state Like:" + datosPet_Post.isLike(),Toast.LENGTH_LONG).show();
            }
        });

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

        datoPetViewHolder.btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileView = new Intent(activity, ProfileView.class);
                profileView.putExtra("petSelected", datosPet_Post);
                activity.startActivity(profileView);
            }
        });

        datoPetViewHolder.btn_chat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataPet = new Bundle();
                dataPet.putString("name", datosPet_Post.getName());
                dataPet.putString("breed", datosPet_Post.getBreed());
                dataPet.putString("profile_pic", datosPet_Post.getProfilePic());

                Intent chatWindow = new Intent(activity, ChatWindow.class);
                chatWindow.putExtras(dataPet);
                activity.startActivity(chatWindow);
            }
        }));
    }

    @Override
    public int getItemCount() {
        //nos entrega la cantida de item de nuestra lista que se manejaran
        return ListaPets.size();
    }

    public static class PetsViewHolder extends RecyclerView.ViewHolder{

        TextView namePet, breed, numberLikes;
        ImageView RecentPostPic, StateLike, btn_comment, btn_shared, btn_chat;
        LinearLayout container_comment, btn_profile;
        CircularImageView img_profile;

        public PetsViewHolder(@NonNull View itemView) {
            super(itemView);
            img_profile = (CircularImageView) itemView.findViewById(R.id.img_profile);
            namePet =(TextView) itemView.findViewById(R.id.nombre);
            breed =(TextView) itemView.findViewById(R.id.raza);
            numberLikes =(TextView) itemView.findViewById(R.id.contadorLikes);
            RecentPostPic =(ImageView) itemView.findViewById(R.id.img_posted_profile);
            StateLike = (ImageView) itemView.findViewById(R.id.like);
            btn_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            btn_chat = (ImageView) itemView.findViewById(R.id.btn_chat);
            container_comment = (LinearLayout) itemView.findViewById(R.id.container_comment);
            btn_profile = (LinearLayout) itemView.findViewById(R.id.btn_profile);

            container_comment.setVisibility(View.GONE);
        }
    }
}
