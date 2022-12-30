package edu.uda.pets.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import edu.uda.pets.POJO.DataPostedPic;
import edu.uda.pets.R;

public class DialogPreviewImg {
    public DialogPreviewImg(Context context, DataPostedPic dataPostedPic){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_preview_img);

        final ImageView imagePosted = (ImageView) dialog.findViewById(R.id.img_preview);
        int id_img_preview = context.getResources().getIdentifier(dataPostedPic.getPic(), "drawable", context.getPackageName());
        imagePosted.setImageResource(id_img_preview);

        dialog.show();
    }

}
