package ec.blcode.stickerswapp.CodeAddSticker2Wapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ec.blcode.stickerswapp.CodeAddSticker2Wapp.WhatsAppBasedCode.StickerPack;

public class DataArchiver {

    private static int BUFFER = 8192;

    public static boolean writeStickerBookJSON(List<StickerPack> sb, Context context)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences("ListPacksContentProvider", Context.MODE_PRIVATE);
            String writeValue = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create()
                    .toJson(
                            sb,
                            new TypeToken<ArrayList<StickerPack>>() {}.getType());

            SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putString("stickerbook", writeValue);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public static <TypeToken> ArrayList<StickerPack> readStickerPackJSON(Context context)
    {
        SharedPreferences mSettings = context.getSharedPreferences("ListPacksContentProvider", Context.MODE_PRIVATE);

        String loadValue = mSettings.getString("stickerbook", "");
        Type listType = new   com.google.gson.reflect.TypeToken<ArrayList<StickerPack>>() {}.getType();
        return new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create()
                .fromJson(loadValue, listType);
    }

    public static void EditUpdateVersionStickerPack(Context context, String version){
        ArrayList<StickerPack> TempListStickerPacks = new ArrayList<>();
        StickerPack TempStickerPAck = readStickerPackJSON( context).get(0);
        TempStickerPAck.setImageDataVersion(version);
        TempListStickerPacks.add(TempStickerPAck);
        /*if (version.equals(TempStickerPAck.getImageDataVersion())){
            try {
                SharedPreferences mSettings = context.getSharedPreferences("StickerMaker", Context.MODE_PRIVATE);
                String writeValue = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create()
                        .toJson(TempListStickerPacks,
                                new TypeToken<ArrayList<StickerPack>>() {}.getType());
                SharedPreferences.Editor mEditor = mSettings.edit();
                mEditor.clear();
                mEditor.putString("stickerbook", writeValue);
                mEditor.apply();
                Toast.makeText(context,"Version Pack Update: " + TempStickerPAck.getImageDataVersion(),Toast.LENGTH_SHORT).show();
               // return true;
            }
            catch(Exception e)
            {
                Toast.makeText(context,"Version Pack No update",Toast.LENGTH_SHORT).show();
                //return false;
            }
        }*/
        //return false;
    }

    public static void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public static void stickerPackToJSONFile(StickerPack sp, String path, Context context) {
        try {
            String writeValue = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create()
                    .toJson(
                            sp,
                            StickerPack.class);
            //ileOutputStream fileOutputStream = new FileOutputStream(path,true);
            //Se crea un creador de texto, El fileOutputStream se crea para poder realizar append, el file se crea para
            //realizar el path de la ruta, y colocarl el nombre del fichero
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(
                    new File(path, sp.getIdentifier()+".json"),true));
            outputStreamWriter.write(writeValue);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private static StickerPack JSONFileToStickerPack(String id, String path, Context context) {

        String ret = "";

        try {

            InputStream inputStream = new FileInputStream(new File(path, id+".json"));


            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create()
                .fromJson(ret, StickerPack.class);
    }


    public static class UriSerializer implements JsonSerializer<Uri> {
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public static class UriDeserializer implements JsonDeserializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType,
                               final JsonDeserializationContext context) throws JsonParseException {
            return Uri.parse(src.toString().replace("\"", ""));
        }
    }

}
