package ec.blcode.stickerswapp.CodeAddSticker2Wapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;

import static ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.CopyResource.getUriToResource;

public class SaveFillSticker{
    private InterfazSaveStickerFill interfazSaveStickerFill;

    public SaveFillSticker(InterfazSaveStickerFill interfazSaveStickerFill) {
        this.interfazSaveStickerFill = interfazSaveStickerFill;
    }

    public void SaveImageFill(Context context, String identifier, int IdResource, String nameSticker ) {
            // ProgressDialog dialog = new ProgressDialog(context);
            Uri uri = getUriToResource(context, IdResource);
            Bitmap resource = BitmapFactory.decodeResource(context.getResources(), IdResource);
            resource = Bitmap.createScaledBitmap(resource, 512, 512, true);
            Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
            Matrix matrix = new Matrix();
            Canvas canvas = new Canvas(bitmap1);
            canvas.drawColor(Color.TRANSPARENT);
            matrix.postTranslate(
                    canvas.getWidth()/2  - resource.getWidth()/2 ,
                    canvas.getHeight()/2  - resource.getHeight()/2
            );
            canvas.drawBitmap(resource, matrix, null);
            interfazSaveStickerFill.UriSaveStickerFill(SaveTryImage(bitmap1,nameSticker,
                    identifier, context));
            //Toast.makeText(context,"Saved Tray Image Pack", Toast.LENGTH_SHORT).show();
            //dialog.dismiss();
    }


    private Uri SaveTryImage(Bitmap finalBitmap, String name, String identifier, Context context) {
            Uri uri=null;
            String path = context.getFilesDir()+"/"+identifier; //crea la carpeta con el nombre de la categoria
            File myDir = new File(path);
            if (!myDir.isDirectory()) {
                myDir.mkdirs();
            }
            String fname = "Sticker_Fill_" + name + ".webp";
            File file = new File(myDir, fname);

            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.WEBP, 80, out);
                out.flush();
                out.close();
                uri= Uri.fromFile(new File(path,fname));
                return uri;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uri;
        }

}
