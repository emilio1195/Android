package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ec.blcode.stickerswapp.R;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.PutTextoPolitica;

public class DialogPoliticaPrivacidad {
    //Context context;
    TextView txtPolitica;
    Button btnAceptar, btnCancelar;
    Dialog dialog;

    public DialogPoliticaPrivacidad (final Context context){
        //this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(false); //para que no sea cancelable al tocar fuera de la pantalla
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_politica);
        btnAceptar = (Button) dialog.findViewById(R.id.btnAcceptP);
        btnCancelar = (Button) dialog.findViewById(R.id.btnCancelarP);
        txtPolitica = (TextView) dialog.findViewById(R.id.txtPolitica);

        PutTextoPolitica(context, txtPolitica);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
