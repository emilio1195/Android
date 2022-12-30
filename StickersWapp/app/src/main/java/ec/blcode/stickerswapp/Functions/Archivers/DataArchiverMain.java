package ec.blcode.stickerswapp.Functions.Archivers;

import static ec.blcode.stickerswapp.Functions.Constantes.KEY_BOOK_CATEGORIES;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_BOOK_DATA_APP;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_BOOK_STICKERS_PURCHASED;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_BOOK_YUANES;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_CATEGORIAS;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_STICKERS_PURCHASED;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.R;

public class DataArchiverMain {

    public static boolean writeDataPacksJSON(Context context, ArrayList<DataPack> dataPacks)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_CATEGORIES, Context.MODE_PRIVATE);
            String writeValue = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create()
                    .toJson(
                            dataPacks,
                            new TypeToken<ArrayList<DataPack>>() {}.getType());

            SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putString(KEY_CATEGORIAS, writeValue);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }

    public static <TypeToken> ArrayList<DataPack> readDataPacksJSON(Context context)
    {
        SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_CATEGORIES, Context.MODE_PRIVATE);

        String loadValue = mSettings.getString(KEY_CATEGORIAS, "");
        Type listType = new   com.google.gson.reflect.TypeToken<ArrayList<DataPack>>() {}.getType();

        return new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create()
                .fromJson(loadValue, listType);
    }

    public static boolean writeYuanes(Context context, String KEY, int yuanesSalario)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_YUANES, Context.MODE_PRIVATE);
            int writeValue = yuanesSalario;

            SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putInt(KEY, writeValue);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }

    public static int readYuanes(Context context, String KEY)
    {
        SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_YUANES, Context.MODE_PRIVATE);

        int loadValue = mSettings.getInt(KEY, 0);

        return loadValue;
    }

    public static boolean writeStickerPurchased(Context context, ArrayList<DataSticker> ListdataSticker)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_STICKERS_PURCHASED, Context.MODE_PRIVATE);
            String writeValue = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create()
                    .toJson(
                            ListdataSticker,
                            new TypeToken<ArrayList<DataSticker>>() {}.getType());

            SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putString(KEY_STICKERS_PURCHASED, writeValue);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }



    public static <TypeToken> ArrayList<DataSticker> readStickersPurchased(Context context)
    {
        SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_STICKERS_PURCHASED, Context.MODE_PRIVATE);

        String loadValue = mSettings.getString(KEY_STICKERS_PURCHASED, null);
        Type listType = new   com.google.gson.reflect.TypeToken<ArrayList<DataSticker>>() {}.getType();

        return new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create()
                .fromJson(loadValue, listType);
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

    public static boolean writeBooleanData(Context context, String KEY, Boolean status)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_DATA_APP, Context.MODE_PRIVATE);
              SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putBoolean(KEY, status);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }

    public static Boolean readBooleanData(Context context, String KEY)
    {
        SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_DATA_APP, Context.MODE_PRIVATE);

        boolean loadValue = mSettings.getBoolean(KEY, false);

        return loadValue;
    }

    public static boolean writeDataAppText(Context context, String KEY, String Data)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_DATA_APP, Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putString(KEY, Data);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }

    public static String readDataAppText(@NonNull Context context, String KEY)
    {
        SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_DATA_APP, Context.MODE_PRIVATE);

        String loadValue = mSettings.getString(KEY, "");

        return loadValue;
    }

    public static boolean writeDataAppList(Context context, String KEY, Set<String> ListPurchases)
    {
        try {
            SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_DATA_APP, Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSettings.edit();
            mEditor.putStringSet(KEY, (Set<String>) ListPurchases);
            mEditor.apply();
            return true;
        }
        catch(Exception e)
        {
            FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }

    public static ArrayList<String> readDataAppList(Context context, String KEY)
    {
        SharedPreferences mSettings = context.getSharedPreferences(KEY_BOOK_DATA_APP, Context.MODE_PRIVATE);

        ArrayList<String> loadValueList = (ArrayList<String>) mSettings.getStringSet(KEY, Collections.<String>emptySet());

        return loadValueList;
    }


    public static void PutTextoPolitica(Context context, TextView textView){
        InputStream inputStream = null;
        try{
            inputStream = context.getResources().openRawResource(R.raw.politica_privacidad);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String lineaTexto = "";
            StringBuilder concatenarTexto = new StringBuilder();
            if (inputStream != null){
                while( (lineaTexto = bufferedReader.readLine()) != null){
                    if(lineaTexto.equals("*")){
                        concatenarTexto.append("\n");
                    }else
                        concatenarTexto.append(lineaTexto + "\n");
                }
                textView.setText(concatenarTexto);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
    }
}
