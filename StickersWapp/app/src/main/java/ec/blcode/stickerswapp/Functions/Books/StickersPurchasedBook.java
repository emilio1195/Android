package ec.blcode.stickerswapp.Functions.Books;

import android.content.Context;

import java.util.ArrayList;

import ec.blcode.stickerswapp.POJO.DataSticker;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readStickersPurchased;

public class StickersPurchasedBook {
    private static ArrayList<DataSticker> dataStickersList;
    static Context myContext;

    public static ArrayList<DataSticker> getListStickersScript(){
        return dataStickersList;
    }

    public  static void AddNewStickerToScript(Context context, DataSticker dataSticker){
       // if(!WithListedCategory(dataSticker.getStickerNombre(), dataSticker.getStickerUrl(), context))
            dataStickersList.add(dataSticker);
    }

    public static ArrayList<DataSticker> getDataStickersList(Context context){
        myContext = context;
        if (CheckListStickersJSON())
            return dataStickersList;
        else
            return new ArrayList<>();
    }


  /*  public static DataPack getPackWithCategoria(String Categoria){

        for (DataPack dataPack : dataStickersList){
            if(dataPack.getPackName().equals(Categoria)){
                return dataPack;
            }
        }
        return null;
    }*/

    public  static  Boolean WithListedCategory (String StickerId, String URLSticker, Context context){
        myContext = context;
        if (CheckListStickersJSON()) {
            for (DataSticker dataSticker : dataStickersList) {
                if (dataSticker.getStickerId().equals(StickerId)) {
                    if (dataSticker.getStickerUrl().equals(URLSticker))
                        return true;
                }
            }
        }else{
            return false;
        }
        return false;
    }

    private static Boolean CheckListStickersJSON(){
        ArrayList<DataSticker> dataStickersL = null;
        if (dataStickersList == null)
            dataStickersL = readStickersPurchased(myContext);
        else if (dataStickersList != null)
            return true;

        if(dataStickersL != null) {
            dataStickersList = dataStickersL;
            return true;
        }
        else if(dataStickersL == null && dataStickersList == null){
            dataStickersList = new ArrayList<>();
            return false;
        }else if(dataStickersL.size() == 0 && dataStickersList == null){
            dataStickersList = new ArrayList<>();
            return false;
        }
        return false;
    }
}
