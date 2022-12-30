package ec.blcode.stickerswapp.Adapter;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeDataAppText;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_LINK_ANDROID_APP;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.R;
import ec.blcode.stickerswapp.View.ViewStickerStatics.View_StickersPack;

public class AdapterListPackStatics extends RecyclerView.Adapter<AdapterListPackStatics.PacksViewHolder> {
    private final Activity activity;
    //Context context;
    ArrayList<DataPack> ListPacks;
    //BaseDatosFavoritos dbF;

    public AdapterListPackStatics(ArrayList<DataPack> listPacks, Activity activity) {
        ListPacks = listPacks;
        this.activity = activity;
    }

    @Override
    public PacksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se encargara de crear la Vista y retornarla
        //Se utilizara el Inflate, el cual se encarga de construir el layout, es decir ir inflando nuestras vistas
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pack_category,parent,false);
        //dbF= new BaseDatosFavoritos(activity);
        return new PacksViewHolder(vista); //se retorna este objeto con el fin de poder colocar el parametro y usar las vistas.
    }

    public static class PacksViewHolder extends RecyclerView.ViewHolder{
        //En esta clase se declaran los vistas de cada elemento del cardview
        TextView txtCategoria;
        ImageView imgTryIcon;
        CardView viewCard;
        ProgressBar pbDownloading;
        public PacksViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategoria =(TextView) itemView.findViewById(R.id.txtCategoria);
            imgTryIcon = (ImageView) itemView.findViewById(R.id.imgTrayicon);
            viewCard = (CardView) itemView.findViewById(R.id.cvCategory);
            pbDownloading = (ProgressBar) itemView.findViewById(R.id.pbDownloadIcon);

        }
    }

    public void onBindViewHolder(@NonNull final PacksViewHolder dataPackViewHolder, int position) {

        // Asocia las lista de datos con cada view y en nuestro ViewHolder
        final DataPack dataPack = ListPacks.get(position); //guardamos la posicion de la lista en el obj
        //en el ViewHolder tenemos inicializada las vistas por lo cual, seteamos sus valores en este metodo, obteniendo del listado datos
        if(!dataPack.getPackLinkPlayStore().equals(""))
            writeDataAppText(activity, KEY_LINK_ANDROID_APP, dataPack.getPackLinkPlayStore());
        final String categoria = dataPack.getPackName();
        dataPackViewHolder.txtCategoria.setText(categoria);
        Glide.with(activity).load(dataPack.getPackTrayIcon())
                //.placeholder(R.drawable.spinner).error(R.drawable.ic_broken_image)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        dataPackViewHolder.pbDownloading.setVisibility(View.GONE);
                        dataPackViewHolder.imgTryIcon.setVisibility(View.VISIBLE);
                        dataPackViewHolder.imgTryIcon.setImageResource(R.drawable.ic_broken_image);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        dataPackViewHolder.pbDownloading.setVisibility(View.GONE);
                        dataPackViewHolder.imgTryIcon.setVisibility(View.VISIBLE);

                        return false;
                    }
                })
                .into(dataPackViewHolder.imgTryIcon);
        ////////////////////////////////////////////////////////////////////

        dataPackViewHolder.viewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent windowSeeMore = new Intent(activity, View_StickersPack.class);
                windowSeeMore.putExtra("CategoriaPack",categoria);
                activity.startActivity(windowSeeMore);
            }
        });
    }

    @Override
    public int getItemCount() {
        //nos entrega la cantida de item de nuestra lista que se manejaran
        return ListPacks.size();
    }

}