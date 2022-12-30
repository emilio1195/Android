package ec.blcode.stickerswapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.DataBase.IFCloudNewData;
import ec.blcode.stickerswapp.Functions.Archivers.InterfazOpYuan;
import ec.blcode.stickerswapp.Functions.Dialogos.CuadroDialogoBuy;
import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.R;

//se realiza una extension de la misma clase con el objetivo de lograr el reciclaje
public class AdapterListaStickers extends RecyclerView.Adapter<AdapterListaStickers.StickersViewHolder> implements IFCloudNewData {
    private final Activity activity;
    Context context;
    ArrayList<DataSticker> ListaStickers;
    //BaseDatosFavoritos dbF;
    InterfazOpYuan ViewInterfaz;
    ProgressDialog progressDialogChange, progressDialogDelete;
    private ProgressDialog progressDialogClone;

    public AdapterListaStickers(ArrayList<DataSticker> listaStickers, Activity activity,  InterfazOpYuan ViewInterfaz) {
        ListaStickers = listaStickers;
        this.activity = activity;
        this.ViewInterfaz = ViewInterfaz;
    }


    @Override
    public StickersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se encargara de crear la Vista y retornarla
        //Se utilizara el Inflate, el cual se encarga de construir el layout, es decir ir inflando nuestras vistas
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sticker,parent,false);
        //dbF= new BaseDatosFavoritos(activity);
        context=activity;
        return new StickersViewHolder(vista); //se retorna este objeto con el fin de poder colocar el parametro y usar las vistas.
    }

    public static class StickersViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        //En esta clase se declaran los vistas de cada elemento del cardview
        //TextView descripcionSticker,
        TextView precio, txtDescargas, txtIdSticker;
        ProgressBar pbDownloading;
        ImageView imgSticker;
        Button btnComprar;
        CardView viewCard;
        public StickersViewHolder(@NonNull View itemView) {
            super(itemView);
           // descripcionSticker =(TextView) itemView.findViewById(R.id.txtdescripcion);
            precio =(TextView) itemView.findViewById(R.id.txtPrecio);
            imgSticker =(ImageView) itemView.findViewById(R.id.imgSticker);
            txtIdSticker =(TextView) itemView.findViewById(R.id.txtIdStickerPreview);
            txtDescargas =(TextView) itemView.findViewById(R.id.txtdownloads);
            btnComprar = (Button) itemView.findViewById(R.id.btnComprar);
            viewCard = (CardView) itemView.findViewById(R.id.cvSticker);
            pbDownloading = (ProgressBar) itemView.findViewById(R.id.pbDownLoadingImage);
            viewCard.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Selecciona una Acción");
            menu.add(this.getAdapterPosition(), 300, 0, "Cambiar de Categoria");//groupId, itemId, order, title
            menu.add(this.getAdapterPosition(),301, 1, "Clonar Sticker");
            menu.add(this.getAdapterPosition(),302, 2, "Eliminar Sticker");
        }
    }

    public void onBindViewHolder(@NonNull final StickersViewHolder dataStickerViewHolder, int position) {
        // Asocia las lista de datos con cada view y en nuestro ViewHolder
        final DataSticker dataSticker = ListaStickers.get(position); //guardamos la posicion de la lista en el obj tipo DatosPets
        //en el ViewHolder tenemos inicializada las vistas por lo cual, seteamos sus valores en este metodo, obteniendo del listado datos
        //dataStickerViewHolder.imgSticker.setImageResource(dataSticker.getImgSticker());
        Glide.with(activity).load(dataSticker.getStickerUrl())
              //  .placeholder(R.drawable.spinner).error(R.drawable.ic_broken_image)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        dataStickerViewHolder.pbDownloading.setVisibility(View.INVISIBLE);
                        dataStickerViewHolder.imgSticker.setVisibility(View.VISIBLE);
                        dataStickerViewHolder.imgSticker.setImageResource(R.drawable.ic_broken_image);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        dataStickerViewHolder.pbDownloading.setVisibility(View.INVISIBLE);
                        dataStickerViewHolder.imgSticker.setVisibility(View.VISIBLE);

                        return false;
                    }
                })
                .into(dataStickerViewHolder.imgSticker);

        //dataStickerViewHolder.descripcionSticker.setText(dataSticker.getStickerNombre() + "\n" + dataSticker.getStickerCredito() ); //+ "( Descargas: " + dataSticker.getCantidadDescargas() + " )"

        dataStickerViewHolder.precio.setText(dataSticker.getStickerPrecio());
        dataStickerViewHolder.txtDescargas.setText(FormatoDescargas(dataSticker.getStickerDescargas()));
        int fin = dataSticker.getStickerId().indexOf(".");
        dataStickerViewHolder.txtIdSticker.setText(dataSticker.getStickerId().substring(0,fin+7));

        dataStickerViewHolder.viewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CuadroDialogoBuy(context, dataSticker, ViewInterfaz);
                // Toast.makeText(activity,"te Gusta: " + dataSticker.getNombre() + ", N likes: " + dataSticker.getCountLikes() + ", state Like:" + dataSticker.isLike(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onViewRecycled(@NonNull StickersViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.setOnLongClickListener(null);
    }

    private String FormatoDescargas(int descargas) {
        if (descargas >= 1000 && descargas < 1000000){
            float Fdescargas= (float) descargas / 1000;
            return obtieneDosDecimales(Fdescargas) + "k";
        }else  if (descargas >= 1000000){
            float Fdescargas= (float) descargas / 1000000;
            return obtieneDosDecimales(Fdescargas) + "M";
        }
        return descargas+"";

    }
    private String obtieneDosDecimales(float valor){
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }

    @Override
    public int getItemCount() {
        //nos entrega la cantida de item de nuestra lista que se manejaran
        return ListaStickers.size();
    }

    public  void deleteSticker(int position){
        SetProgressDialog progressDialog = new SetProgressDialog(context);
        DataSticker dataSticker = ListaStickers.get(position);
        progressDialogDelete = progressDialog.getProgressDialog("Eliminando Sticker",
                "Espere, se esta eliminando el sticker de esta categoria"+
                        "\nId Sticker: "+ dataSticker.getStickerId());
        progressDialogDelete.show();

        CloudFirebase cloudFirebase = new CloudFirebase(context, this);
        cloudFirebase.deleteSticker(dataSticker, position);
        //Toast.makeText(context, "Eliminando...\n"+ dataSticker.getStickerId(), Toast.LENGTH_LONG).show();
       // notifyDataSetChanged();
    }

    public  void changeCategorySticker(int position, String category){
        SetProgressDialog progressDialog = new SetProgressDialog(context);
        DataSticker dataSticker = ListaStickers.get(position);

        progressDialogChange = progressDialog.getProgressDialog("Cambiando Categoria",
                "Espere, se esta cambiando el sticker a la categoria: "+ category +
                "\nId Sticker: "+ dataSticker.getStickerId());
        progressDialogChange.show();
        //txtId.setText( dataSticker.getStickerId());
        CloudFirebase cloudFirebase = new CloudFirebase(context, this);
        cloudFirebase.changeCategoriaSticker(dataSticker, category, position);
        //Toast.makeText(context, "Cambiando...\n" + dataSticker.getStickerId(), Toast.LENGTH_SHORT).show();
        //notifyDataSetChanged();
    }

    public  void cloneSticker(int position, String category){
        SetProgressDialog progressDialog = new SetProgressDialog(context);
        DataSticker dataSticker = ListaStickers.get(position);

        progressDialogClone = progressDialog.getProgressDialog("Clonando Sticker",
                "Espere, se esta clonando el sticker en la categoria: "+ category +
                        "\nId Sticker: "+ dataSticker.getStickerId());
        progressDialogClone.show();
        //txtId.setText( dataSticker.getStickerId());
        CloudFirebase cloudFirebase = new CloudFirebase(context, this);
        cloudFirebase.cloneSticker(dataSticker, category);
        //Toast.makeText(context, "Cambiando...\n" + dataSticker.getStickerId(), Toast.LENGTH_SHORT).show();
        //notifyDataSetChanged();
    }

    @Override
    public void DataPackUpdated(ArrayList<DataPack> dataPacksList) {

    }

    @Override
    public void DeleteSticker(Boolean isdeleted, int position) {
        progressDialogDelete.dismiss();
        if(isdeleted) {
            ListaStickers.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public void ChangeStickerCategory(Boolean isChanged) {
        progressDialogChange.dismiss();
        if(isChanged) {
            SetProgressDialog progressDialog = new SetProgressDialog(context);
            progressDialogDelete = progressDialog.getProgressDialog("Eliminando Sticker",
                    "Espere, se esta eliminando el sticker de esta categoria");
            progressDialogDelete.show();
        }
    }

    @Override
    public void CloneStickerCategory(Boolean isCloned) {
        progressDialogClone.dismiss();
        SetProgressDialog progressDialog = new SetProgressDialog(context);
        if(isCloned) {
            progressDialog.getAlertDialog("Clonacion Completada", "Se ha clonado el sticker correctamente");
        }else
            progressDialog.getAlertDialog("Clonacion Fallida", "No se pudo clonar hubo un problema.");
    }
}
