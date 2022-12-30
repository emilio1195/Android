package ec.blcode.stickerswapp.CodeAddSticker2Wapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;

import ec.blcode.stickerswapp.POJO.DataSticker;

public class SaveTrayImage  {
    private InterfazSaveTrayImage interfazSaveTrayImage;

    public SaveTrayImage(InterfazSaveTrayImage interfazSaveTrayImage) {
        this.interfazSaveTrayImage = interfazSaveTrayImage;
    }

    public void SaveImageTrayFile(final Context context, final DataSticker dataSticker) {
       // ProgressDialog dialog = new ProgressDialog(context);
        Log.d("TRAYImage: ",dataSticker.getStickerTryIconURL());
        ///Busca si la imagen se encuentra en la cache par ano descargar de nuevo la imagen
        Glide.with(context).load(dataSticker.getStickerTryIconURL())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).preload();
        /*dialog.setMessage("please wait >* ...");
        dialog.show();*/

        Glide.with(context).asBitmap().load(dataSticker.getStickerTryIconURL()).timeout(500)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                        //dialog.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        resource = Bitmap.createScaledBitmap(resource, 96, 96, true);
                        Bitmap bitmap1 = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
                        Matrix matrix = new Matrix();
                        Canvas canvas = new Canvas(bitmap1);
                        canvas.drawColor(Color.TRANSPARENT);
                        matrix.postTranslate(
                                canvas.getWidth()/2  - resource.getWidth()/2 ,
                                canvas.getHeight()/2  - resource.getHeight()/2
                        );
                        canvas.drawBitmap(resource, matrix, null);
                        interfazSaveTrayImage.UriSaveTrayImage(SaveTryImage(bitmap1,
                                                                            dataSticker.getStickerCategoria(), context), dataSticker);
                        //Toast.makeText(context,"Saved Tray Image Pack", Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                        return false;
                    }
                })
                .submit();
        /*if (interfazSaveTrayImage == null){
            Toast.makeText(context,"No se Pudo descargar Icono Pack", Toast.LENGTH_SHORT).show();
        }*/
    }


    private   Uri SaveTryImage(Bitmap finalBitmap, String identifier, Context context) {
        Uri uri=null;
        String path = context.getFilesDir()+"/"+identifier; //crea la carpeta con el nombre de la categoria
        File myDir = new File(path);
        if (!myDir.isDirectory()) {
            myDir.mkdirs();
        }
        String fname = "trayImage_" + identifier + ".png";
        File file = new File(myDir, fname);
        //file -> /data/user/0/ec.blcode.contentproviderejemplo/files/Groceria/Groceria/trayImage_Groceria.png
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 89, out);
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


