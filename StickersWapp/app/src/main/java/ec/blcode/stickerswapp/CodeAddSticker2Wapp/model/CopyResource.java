package ec.blcode.stickerswapp.CodeAddSticker2Wapp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyResource {

    public static Uri CopiarRecurso(Context context, String identifier, int IdResource, String nameSticker) {
        String path = context.getFilesDir()+"/"+identifier; //crea la carpeta con el nombre de la categoria
        File myDir = new File(path);
        if (!myDir.isDirectory()) {
            myDir.mkdirs();
        }
        String fname = nameSticker + ".webp";
        File outFile = new File(myDir, fname);

        File inFile = new File(String.valueOf(getUriToResource(context,IdResource)));
        Uri uri;
        try{

            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out =new FileOutputStream(outFile);

            byte[] buffer = new byte[300];
            int c;

            while( (c = in.read(buffer) ) != -1) //debe ser hasta -1 dode indica que ya no hya mas datos a leer
                out.write(buffer, 0, c);

            out.flush();
            in.close();
            out.close();
            uri= Uri.fromFile(new File(path,fname));
            return uri;

        } catch(IOException e) {

            Log.e("COPIAR ARCHIVOS> ", "Hubo un error de entrada/salida!!!");
        }
        return null;
    }

    public static final Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {

        Resources res = context.getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));

        return resUri;
    }
}
