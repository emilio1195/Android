package ec.blcode.stickerswapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.DataBase.IFCloudNewData;
import ec.blcode.stickerswapp.Functions.Dialogos.DialogoImageZoomAdd;
import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB.IFPresenter_AddImages;
import ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB.Presenter_Add_Sticker;
import ec.blcode.stickerswapp.Presenter.PresenterListPackStatics.Presenter_ListPack;
import ec.blcode.stickerswapp.R;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.getDataPacksList;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.getDataPacksListUpdated;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.getPackWithCategoria;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_NAME_LASTN;

public class AdapterListImagesLoad extends RecyclerView.Adapter<AdapterListImagesLoad.ImagesLoadViewHolder> implements IFPresenter_AddImages, IFCloudNewData {
    ArrayList<Uri> listaImagesLoad;
    ArrayList<Float> listSizesImages;
    //List<Integer> indexUploaded;
    //int totalUris;

    Context context;
    Activity activity;
    private String Categoria = "", Precio = "", UrlSticker = "", UrlTaryIcon = "";
    DataPack CategoriaPick = null;
    DataSticker dataSticker;
    SetProgressDialog setProgressDialog;
    ProgressBar pbWaitUpload, pbLoadingData;
    ImageView imgCloudSt, imgStorageSt;
    TextView txtUrlSt;
    LinearLayout containerUploadGlobal;
    ImagesLoadViewHolder imagesLoadViewHolder;
    String user;
    Button btnCloneGlobal, btnUploadGlobal;
    Presenter_Add_Sticker presenter_add_sticker;
    Presenter_ListPack presenter_listPack;
    private ProgressDialog progressDialog;

    public AdapterListImagesLoad(ArrayList<Uri> listaImagesLoad, ArrayList<Float> listSizesImages, Activity activity) {
        this.listaImagesLoad = listaImagesLoad;
        this.listSizesImages = listSizesImages;
        //indexUploaded = new ArrayList<>();
        this.activity = activity;
        this.context = activity;
        user = readDataAppText(context, KEY_NAME_LASTN).trim().replace(" ", "_");
        //totalUris = readYuanes(context, KEY_ID_IMG);
    }

    @Override
    public ImagesLoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Se encargara de crear la Vista y retornarla
        //Se utilizara el Inflate, el cual se encarga de construir el layout, es decir ir inflando nuestras vistas
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_add_varios_stickers, parent,false);
        setProgressDialog = new SetProgressDialog(context);
        return new ImagesLoadViewHolder(vista); //se retorna este objeto con el fin de poder colocar el parametro y usar las vistas.
    }

    public static class ImagesLoadViewHolder extends RecyclerView.ViewHolder{
        //En esta clase se declaran los vistas de cada elemento del cardview
        ProgressBar pbWait, pbUploading;
        TextView txtSize, txtUrl;
        EditText edDescripcion, edCredito;
        ImageView imgSticker, imgCloud, imgStorage;
        Button btnUpload, btnClone;
        Spinner spnPrecios, spnCategorias;
        LinearLayout containerUpload;
        ArrayList<String> ListPrecios = new ArrayList<>(Arrays.asList("Precio", "0","15","30","60","90","120","300", "500"));
        ArrayList<DataPack> dataPacksList;
        ArrayAdapter<String> adapter;
        List<String> CategoriasList;
        public ImagesLoadViewHolder(@NonNull View itemView) {
            super(itemView);
            edDescripcion =(EditText) itemView.findViewById(R.id.edtDescripcion);
            edCredito =(EditText) itemView.findViewById(R.id.edtCreditoSt);
            txtUrl =(TextView) itemView.findViewById(R.id.txtUrlS);
            txtSize =(TextView) itemView.findViewById(R.id.txtsizeSt);
            imgSticker =(ImageView) itemView.findViewById(R.id.imgStickerSrc);
            imgStorage =(ImageView) itemView.findViewById(R.id.imgStorage);
            imgCloud =(ImageView) itemView.findViewById(R.id.imgCloud);
            spnPrecios = (Spinner) itemView.findViewById(R.id.spinPrecioS);
            spnCategorias = (Spinner) itemView.findViewById(R.id.spinCategoriaS);
            btnUpload = (Button) itemView.findViewById(R.id.btnUploadedData);
            btnClone = (Button) itemView.findViewById(R.id.btnCLoneData);
            pbWait = (ProgressBar) itemView.findViewById(R.id.pbLoading);
            pbUploading = (ProgressBar) itemView.findViewById(R.id.pbLoadProgress);
            containerUpload = (LinearLayout)  itemView.findViewById(R.id.containerUpload);

            containerUpload.setVisibility(View.GONE);
            btnClone.setVisibility(View.GONE);
            pbWait.setVisibility(View.GONE);

            edDescripcion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            edCredito.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            ClicSpinnerPrecios();
            dataPacksList = getDataPacksList(itemView.getContext());
            ClicSpinnerCategorias();
        }

        private void ClicSpinnerPrecios()
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(itemView.getContext(), R.layout.spinner_item_own_2, ListPrecios);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPrecios.setAdapter(adapter);
        }
        private void ClicSpinnerCategorias()
        {
            if(CategoriasList == null)
                CategoriasList= new ArrayList<>();
            else
                CategoriasList.clear();

            for(DataPack dataPack : dataPacksList)
                CategoriasList.add(dataPack.getPackName());

            CategoriasList.add(0,"<Refresh List>");
            CategoriasList.add(0,"<Categoria>");
            adapter = new ArrayAdapter<String>(itemView.getContext(), R.layout.spinner_item_own_2, CategoriasList);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategorias.setAdapter(adapter);
           // adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagesLoadViewHolder Viewholder, int position) {

        // Asocia las lista de datos con cada view y en nuestro ViewHolder
        final Uri uriImage = listaImagesLoad.get(position); //guardamos la posicion de la lista en el obj tipo DatosPets
        String SizeImage = listSizesImages.get(position)+"";
        //en el ViewHolder tenemos inicializada las vistas por lo cual, seteamos sus valores en este metodo, obteniendo del listado datos
        //dataStickerViewHolder.imgSticker.setImageResource(dataSticker.getImgSticker());
       /* Glide.with(activity).load(uriImage)
                .placeholder(R.drawable.spinner).error(R.drawable.ic_broken_image)
                .centerCrop().into(Viewholder.imgSticker);*/
        int size = (int) Double.parseDouble(SizeImage);
        if (size > 99) {
            Viewholder.txtSize.setBackgroundColor(context.getResources().getColor(R.color.Cancel));
            Viewholder.txtSize.setTextColor(context.getResources().getColor(R.color.acent_TextColor));
        }

        Viewholder.imgSticker.setImageURI(uriImage);
        Viewholder.txtSize.setText("Size image = " + SizeImage +"" + " Kb");

        Viewholder.imgSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogoImageZoomAdd(context, uriImage);
            }
        });

        Viewholder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size <= 99)
                  uploadData(Viewholder, uriImage, position);
                else
                    Toast.makeText(context, "Imagen supera los 99kb permitidos", Toast.LENGTH_LONG).show();
            }
        });

        Viewholder.btnClone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size <= 99)
                    cloneData(Viewholder, uriImage, position);
                else
                    Toast.makeText(context, "Imagen supera los 99kb permitidos", Toast.LENGTH_LONG).show();
            }
        });

        Viewholder.spnPrecios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listenerSpinnerPrecio(parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Viewholder.spnCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listenerSpinnerCategoria(parent, Viewholder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listaImagesLoad.size();
    }

    private void listenerSpinnerPrecio(AdapterView<?> parent){
        String temp = parent.getSelectedItem().toString();
        if(!temp.equals("<Precio>"))
            Precio = temp;
        else
            Precio = "";

       // Toast.makeText(context, Precio,Toast.LENGTH_SHORT).show();

    }

    private void uploadData(ImagesLoadViewHolder viewholder, Uri uriImage, int position) {
        String descripcion = viewholder.edDescripcion.getText().toString();
        String credito = viewholder.edCredito.getText().toString();

        if(descripcion.equals("")){
            viewholder.edDescripcion.setError("Campo Obligatorio");
        }
        if(credito.equals("")){
            viewholder.edCredito.setError("Campo Obligatorio");
        }
        if(!descripcion.equals("") && !credito.equals("") && !Categoria.equals("") && !Precio.equals("")){
            containerUploadGlobal = viewholder.containerUpload;
            containerUploadGlobal.setVisibility(View.VISIBLE);
            btnUploadGlobal = viewholder.btnUpload;
            btnUploadGlobal.setVisibility(View.GONE);
            btnCloneGlobal = viewholder.btnClone;

            txtUrlSt = viewholder.txtUrl;
            imgCloudSt = viewholder.imgCloud;
            imgStorageSt = viewholder.imgStorage;
            pbWaitUpload = viewholder.pbWait;
            pbWaitUpload.setVisibility(View.VISIBLE);
            pbLoadingData = viewholder.pbUploading;
            String IdSticker = uriImage.getLastPathSegment() + user;
            dataSticker = new DataSticker(IdSticker,descripcion, credito, Categoria, Precio, 0, "", UrlTaryIcon);
            presenter_add_sticker = new Presenter_Add_Sticker(context, this);
            presenter_add_sticker.UpStickerStorage(uriImage, "estaticos", viewholder.pbUploading, IdSticker, position);
        }else{
            Toast.makeText(context, R.string.Empty_Field, Toast.LENGTH_LONG).show();
        }

    }

    private void cloneData(ImagesLoadViewHolder viewholder, Uri uriImage, int position) {
        String descripcion = viewholder.edDescripcion.getText().toString();
        String credito = viewholder.edCredito.getText().toString();
        if(descripcion.equals("")){
            viewholder.edDescripcion.setError("Campo Obligatorio");
        }
        if(credito.equals("")){
            viewholder.edCredito.setError("Campo Obligatorio");
        }
        if(!descripcion.equals("") && !credito.equals("") && !Categoria.equals("") && !Precio.equals("")){
            viewholder.pbWait.setVisibility(View.VISIBLE);
            containerUploadGlobal.setVisibility(View.VISIBLE);
            btnCloneGlobal.setVisibility(View.GONE);
            CloudFirebase cloudFirebase = new CloudFirebase(context, this);
            cloudFirebase.cloneSticker(dataSticker, Categoria);
        }else{
            Toast.makeText(context,  R.string.Empty_Field, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void CompletedStorageSticker(String UrlSticker, boolean Status, int position) {
        if(Status){
            txtUrlSt.setText(UrlSticker);
            imgStorageSt.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_circle_outline_30));
            //notifyDataSetChanged();
            if(dataSticker != null) {
                dataSticker.setStickerUrl(UrlSticker);
                presenter_add_sticker.UploadCloudSticker(dataSticker, position);
            }
            else
                Toast.makeText(context, "dataSticker is null", Toast.LENGTH_LONG).show();
        }else{
            pbWaitUpload.setVisibility(View.GONE);
            pbLoadingData.setProgress(2);
        }

    }

    @Override
    public void CompletedCloudSticker(boolean Status, int position) {
        if(Status){
            imgCloudSt.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_circle_outline_30));
            pbWaitUpload.setVisibility(View.GONE);
            pbLoadingData.setProgress(100);
            timeOut_ContainerUpload();
            btnCloneGlobal.setVisibility(View.VISIBLE);
           /* indexUploaded.add(position);
            if( listaImagesLoad.size() > indexUploaded.size()) {
                totalUris = totalUris - (listaImagesLoad.size() - indexUploaded.size());
                writeYuanes(context, KEY_ID_IMG, totalUris); //Se escribe el ultimo valor de ID
            }*/
        }else{
            pbWaitUpload.setVisibility(View.GONE);
            btnUploadGlobal.setVisibility(View.VISIBLE);
            btnCloneGlobal.setVisibility(View.GONE);
            timeOut_ContainerUpload();
            pbLoadingData.setProgress(70);
            Toast.makeText(context, "Error Upload Cloud, TryAgain", Toast.LENGTH_LONG).show();
        }
       // this.notifyDataSetChanged();
    }

    private void listenerSpinnerCategoria(final AdapterView<?> parent, final ImagesLoadViewHolder Viewholder) {
        this.imagesLoadViewHolder = Viewholder;
        String tempItemSelected = parent.getSelectedItem().toString();
        if (!tempItemSelected.equals("<Categoria>") && !tempItemSelected.equals("<Refresh List>")) {
            Categoria = tempItemSelected;
            CategoriaPick = getPackWithCategoria(context,Categoria);
            UrlTaryIcon = CategoriaPick.getPackTrayIcon();
            //int versionActual = CategoriaPick.getPackVersion();
            //txtVersionAct.setText(versionActual+"");
            //versionNew = versionActual + 1;
            //txtVersionNew.setText(versionNew+"");
        }else if (tempItemSelected.equals("<Categoria>")){
            Categoria="";
            CategoriaPick = null;
        }else if (tempItemSelected.equals("<Refresh List>")) {
            Categoria="";
            CategoriaPick = null;
            imagesLoadViewHolder.dataPacksList =  getDataPacksListUpdated(context);
            progressDialog = setProgressDialog.IncProgressDialog("Update...",
                    "Actualizando Lista de Categorias");
            progressDialog.show();
            //presenter_listPack = new Presenter_ListPack(activity);

            new CountDownTimer(1200, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    progressDialog.incrementProgressBy(12);
                }

                public void onFinish() {
                    // When timer is finished
                    // Execute your code here
                    //se coloca un atarea final
                    imagesLoadViewHolder.ClicSpinnerCategorias();
                    progressDialog.dismiss();
                }

            }.start();
        }
    }

    @Override
    public void DataPackUpdated(ArrayList<DataPack> dataPacksList) {

    }

    @Override
    public void DeleteSticker(Boolean isdeleted, int position) {

    }

    @Override
    public void ChangeStickerCategory(Boolean isChanged) {

    }

    @Override
    public void CloneStickerCategory(Boolean isCloned) {
        pbWaitUpload.setVisibility(View.GONE);
        btnCloneGlobal.setVisibility(View.VISIBLE);
        SetProgressDialog progressDialog = new SetProgressDialog(context);
        if(isCloned) {
            progressDialog.getAlertDialog("Clonacion Completada", "Se ha clonado el sticker correctamente");
            timeOut_ContainerUpload();
        }else
            progressDialog.getAlertDialog("Clonacion Fallida", "No se pudo clonar hubo un problema.");

    }

    public void timeOut_ContainerUpload(){
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                containerUploadGlobal.setVisibility(View.GONE);
            }

        }.start();

    }


}
