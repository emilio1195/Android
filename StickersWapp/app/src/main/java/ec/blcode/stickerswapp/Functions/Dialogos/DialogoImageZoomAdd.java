package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Window;
import android.widget.ImageView;

import ec.blcode.stickerswapp.R;

public class DialogoImageZoomAdd {

    public DialogoImageZoomAdd(Context context, Uri uri) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_preview_image);
        ImageView imgZoom = (ImageView) dialog.findViewById(R.id.imgZoom);
        imgZoom.setImageURI(uri);
        dialog.show();
    }
}
