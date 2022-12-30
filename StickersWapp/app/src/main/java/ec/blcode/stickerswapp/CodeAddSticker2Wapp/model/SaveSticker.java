package ec.blcode.stickerswapp.CodeAddSticker2Wapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;

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

public class SaveSticker {

    private InterfazSaveStickerImage interfazSaveStickerImage;

    public SaveSticker(InterfazSaveStickerImage interfazSaveStickerImage) {
        this.interfazSaveStickerImage = interfazSaveStickerImage;
    }

    public void SaveStickerFile(final Context context, final DataSticker dataSticker) {
        ///Busca si la imagen se encuentra en la cache para no descargar de nuevo la imagen
       Glide.with(context).load(dataSticker.getStickerUrl())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).preload();

        Glide.with(context).asBitmap()//.apply(new RequestOptions().override(512, 512))
                .load(dataSticker.getStickerUrl()).timeout(500)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource
                            dataSource, boolean isFirstResource) {
                        resource = Bitmap.createScaledBitmap(resource, 512, 512, true);
                        Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                        Matrix matrix = new Matrix();
                        Canvas canvas = new Canvas(bitmap1);
                        canvas.drawColor(Color.TRANSPARENT);
                        matrix.postTranslate(
                                canvas.getWidth()/2  - resource.getWidth()/2 ,
                                canvas.getHeight()/2 - resource.getHeight()/2
                        );
                        canvas.drawBitmap(resource, matrix, null);
                        interfazSaveStickerImage.UriSaveStickerImage(SaveSticker(bitmap1, dataSticker.getStickerId(),
                                                                dataSticker.getStickerCategoria(), context));
                        //Toast.makeText(context,"Saved Image Sticker", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }).submit();
        /*if (interfazSaveTrayImage == null)
            Toast.makeText(context,"No se Pudo descargar Image Sticker", Toast.LENGTH_SHORT).show();*/


    }

    private Uri  SaveSticker(Bitmap finalBitmap, String name, String identifier, Context context) {
        Uri uri=null;
        String path = context.getFilesDir()+"/"+identifier;
        File myDir = new File(path);
        if (!myDir.isDirectory()) {
            myDir.mkdirs();
        }
        String fname = name + ".webp";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.WEBP, 89, out);
            out.flush();
            out.close();
            uri = Uri.fromFile(new File(path,fname));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }
}
