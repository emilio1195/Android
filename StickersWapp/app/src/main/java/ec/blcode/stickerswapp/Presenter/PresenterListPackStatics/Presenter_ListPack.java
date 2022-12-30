package ec.blcode.stickerswapp.Presenter.PresenterListPackStatics;

import android.app.Activity;
import android.app.ProgressDialog;

import java.util.ArrayList;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.DataBase.InterfazDataFireStore;
import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.POJO.PackSticker;
import ec.blcode.stickerswapp.View.ViewPackListStatics.Interface_View_ListPackFragment;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeDataPacksJSON;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.AddNewCategoriaToScript;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.UpdateList;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.getDataPacksList;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.getListScript;

public class Presenter_ListPack  implements InterfazDataFireStore {
    Interface_View_ListPackFragment interface_view_listPackFragment;
    CloudFirebase cloudFirebase;
    Activity activity;
    //ArrayList<DataPack> dataPacksList;
    SetProgressDialog setProgressDialog;
    ProgressDialog progressDialog;
    public Presenter_ListPack(Activity activity, Interface_View_ListPackFragment interfazView){
        this.interface_view_listPackFragment = interfazView;
        this.activity=activity;
        getLisPAckCategory();
    }
    public Presenter_ListPack(Activity activity){
        this.interface_view_listPackFragment = null;
        this.activity=activity;
        getLisPAckCategory2();
    }


    private void getLisPAckCategory() {
        cloudFirebase = new CloudFirebase(this, activity);
        /*progressDialog = new ProgressDialog(activity,AlertDialog.THEME_HOLO_DARK);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Cargando Categorias");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        progressDialog.show();*/
        cloudFirebase.readIdsPacksDB("StaticsPacks");
    }

    private void getLisPAckCategory2() {
        cloudFirebase = new CloudFirebase(this, activity);
        cloudFirebase.readIdsPacksDBForUpdate("StaticsPacks");
    }

    @Override
    public void ListDataStickers(ArrayList<DataSticker> listDataStickers) {
        ////no se utiliza por ahora
    }

    @Override
    public void ListDataPack(ArrayList<DataPack> dataPackArrayList) {
        interface_view_listPackFragment.startAdapter(interface_view_listPackFragment.initAdapter(dataPackArrayList));
        //progressDialog.dismiss();
        //interface_view_listPackFragment.startAdapter(interface_view_listPackFragment.initAdapter(dataPackArrayList));
        writeDataPacksJSON(activity,dataPackArrayList);
        ArrayList<DataPack> dataPacks = getDataPacksList(activity);
        //Compara si el script pricado de categorias esta vacio, guardamos los datos del DB
        if ( dataPacks.size() == 0 || dataPacks == null)
             writeDataPacksJSON(activity,dataPackArrayList);
         else if(dataPacks.size() != dataPackArrayList.size()) { // Compara si el tamano de las listas son diferentes para reescribirlas
            for (DataPack dataPack : dataPackArrayList)
                AddNewCategoriaToScript(activity, dataPack); //anade solo los que no existen en el script
            //Escribe solo los packs que no existen
            writeDataPacksJSON(activity, getListScript()); //el getList() entrga una lista actualziada con las nuevas categorias
        }else{ //si las listas son las mismas se actualizan sus datos
            UpdateList(activity, dataPackArrayList);
        }
    }

    @Override
    public void ListStickersPreview(ArrayList<DataSticker> dataListPreview) {

    }

    @Override
    public void ListPacksStickersPreview(ArrayList<PackSticker> listPackSticker) {

    }

    @Override
    public void SuccesfullUpUser(Boolean Uploaded) {

    }


}
