package ec.blcode.stickerswapp.Functions.OperationPdf;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class OperationPdf {


    private static void CopyRawToSDCard(Context context, int id, String path) {
        InputStream in = context.getResources().openRawResource(id);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            in.close();
            out.close();
            Log.i("Satate Copy", "copyFile, success!");
        } catch (FileNotFoundException e) {
            Log.e("Satate Copy", "copyFile FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e("Satate Copy", "copyFile IOException " + e.getMessage());
        }
    }

    public static void openPdfManual(Activity activity){
      //  CopyRawToSDCard(activity, R.raw.eliminar_packs_stickers_wapp_signed,
          //              Environment.getExternalStorageDirectory() + "/eliminar_packs_stickers_wapp_signed.pdf" );
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ "eliminar_packs_stickers_wapp_signed.pdf\"");

        if (pdfFile.exists()){ //Revisa si el archivo existe!
            Uri path = Uri.fromFile(pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //define el tipo de archivo
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent chooser = null;
            Context context = activity;
            try {
                chooser = Intent.createChooser(intent, "Abrir con...");
                context.startActivity(chooser);
                //startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "No existe una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(activity, "No existe archivo! ", Toast.LENGTH_SHORT).show();
        }
    }
}
