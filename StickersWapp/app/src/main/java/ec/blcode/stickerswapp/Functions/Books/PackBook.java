package ec.blcode.stickerswapp.Functions.Books;

import android.content.Context;

import java.util.ArrayList;

import ec.blcode.stickerswapp.POJO.DataPack;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataPacksJSON;

public class PackBook {
    private static ArrayList<DataPack> dataPacksList;
    static Context myContext;

    public static ArrayList<DataPack> getListScript(){
        return dataPacksList;
    }
    public static int getVersionCategoria(Context context, String Categoria){
        myContext = context;
        if (CheckDataPackJSON()){
            int version = getPackWithCategoria(context, Categoria).getPackVersion();
            return version;
        }else{
            return 0;
        }
    }



    public  static void AddNewCategoriaToScript(Context context, DataPack dataPack){
        myContext = context;
        if (CheckDataPackJSON());
            if(!WithListedCategory(dataPack.getPackName()))
                dataPacksList.add(dataPack);
    }

    public static  void UpdateList(Context context, ArrayList<DataPack> ListdataPack){
        dataPacksList = ListdataPack;
    }
    public static ArrayList<DataPack> getDataPacksList (Context context){
        myContext = context;
        if (CheckDataPackJSON())
            return dataPacksList;
        else
            return new ArrayList<>();
    }

    public static ArrayList<DataPack> getDataPacksListUpdated (Context context){
        dataPacksList = readDataPacksJSON(myContext);
        return dataPacksList;
    }

    public static DataPack getPackWithCategoria(Context context, String Categoria){
        myContext = context;

        for (DataPack dataPack : dataPacksList){
            if(dataPack.getPackName().equals(Categoria)){
                return dataPack;
            }
        }
        return null;
    }

    private  static  Boolean WithListedCategory (String Categoria){
        for (DataPack dataPack : dataPacksList){
            if(dataPack.getPackName().equals(Categoria)){
                return true;
            }
        }
        return false;
    }

    private static Boolean CheckDataPackJSON(){
        ArrayList<DataPack> dataPacksL = null;
        if (dataPacksList == null)
             dataPacksL = readDataPacksJSON(myContext);
        else if (dataPacksList != null)
            return true;

        if(dataPacksL != null && dataPacksL.size() != 0) {
            dataPacksList = dataPacksL;
            return true;
        }
        else if(dataPacksL == null && dataPacksList == null){
            dataPacksList = new ArrayList<>();
            return false;
        }
        return false;
    }

}
