package ec.blcode.stickerswapp.Presenter.PresenterStickerStatics;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.DataBase.InterfazDataFireStore;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.POJO.PackSticker;
import ec.blcode.stickerswapp.View.ViewStickerStatics.Interface_View_DetailsPack;
import ec.blcode.stickerswapp.View.ViewStickerStatics.View_StickersPack;

public class Presenter_StickersListStatics implements InterfacePresenterStickerStatics, InterfazDataFireStore {

    Interface_View_DetailsPack IFViewStickerStatics;
    CloudFirebase cloudFirebase;
    Activity activity;
    public Presenter_StickersListStatics(View_StickersPack view_detailsPack, String Category){
        this.IFViewStickerStatics = view_detailsPack;
        this.activity=view_detailsPack;
        if (!Category.equals(""))
            showListStickersStaticsBD(Category);
    }

    public void getListStickersStaticsBD(String Category) { //al implementar este metodo, hace que actue el metodo de la Interfaz de fireStore
                                            //haciendo que el metodo ListDataStickers() se ejecute automaticamente, para enviar la lista al adapter del recyclerView
        cloudFirebase = new CloudFirebase(this, activity);  //realiza una instancia y se coloca la actividad que implementa la interfaz de firestore
        cloudFirebase.readStickersCollectionBD("StaticsPacks", Category, activity);
        //cloudFirebase.readAllDocumentsDB("StickersStatics",activity); // se llama al metodo de lecturade firestore, se coloca el nombre de la coleccion y la actividad para mostrar un Toast
    }

    @Override
    public void showListStickersStaticsBD(String Category) {
        IFViewStickerStatics.initRecyclerView(numberItemsShowScreen(activity));
        getListStickersStaticsBD(Category);
    }

    @Override
    public int numberItemsShowScreen(Context context) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        //int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        return widthPixels / 210; // se divide el ancho de la pantalla para el ancho del cardview del sticker
    }

    @Override  //Este metodo se ejecuta automaticamente al llamar al metodo readDB()
    public void ListDataStickers(ArrayList<DataSticker> listDataStickers) {  //recibimos el Array List de la base de datos
        IFViewStickerStatics.startAdapter(IFViewStickerStatics.initAdapter(listDataStickers)); //enviamos la lista al adapter para crear los cardview
    }

    @Override
    public void ListDataPack(ArrayList<DataPack> dataPackArrayList) {

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

    /*@Override
    public void ListDataPack(String listDataPAck, Map<String, Object> data) {

    }*/

}
