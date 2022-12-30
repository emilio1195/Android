package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ec.blcode.stickerswapp.Functions.Admod.Admod;
import ec.blcode.stickerswapp.Functions.Admod.InterfazAdmod;
import ec.blcode.stickerswapp.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.getYuanVal;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.giftYuanes;
import static ec.blcode.stickerswapp.Functions.Network_Conectivity_Status.isOnlineNet;

public class CuadroDialogoRecompensa implements InterfazAdmod {
    Dialog dialog;
    LinearLayout lyMoreYuans;
    ProgressBar progressBar;
    Button btnCerrar;
    Context context;
    Admod admod;
    //Activity activity;
    TextView txtUpdateMonedas, txtrewarded;

    public CuadroDialogoRecompensa (final Context context, final TextView txtMonedas){
        this.context = context;
        this.txtUpdateMonedas = txtMonedas;
        //this.activity = activity;
        admod = new Admod(this, (Activity) context);
        /////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////PRELOAD BONIFICADO MAIN//////////////////////////////////////////////////////////////////
        if (isOnlineNet())
            admod.initREwardedAd(context.getString(R.string.admob_ad_Rewarded_test));
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public void ShowDialogRewarded (String ValRewardedAnterior) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(false); //para que no sea cancelable al tocar fuera de la pantalla
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_dialog_anuncio);
        btnCerrar = (Button) dialog.findViewById(R.id.btnCerrarDialog);
        lyMoreYuans = (LinearLayout) dialog.findViewById(R.id.lyX2Yuans);
        progressBar =(ProgressBar) dialog.findViewById(R.id.pbDoubleYuans);
        txtrewarded = (TextView) dialog.findViewById(R.id.txtreawrded);
        txtrewarded.setText(ValRewardedAnterior);

        lyMoreYuans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAdclic();
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void ShowAdclic (){
        if (isOnlineNet())
            admod.setRewardedAd();
        else {
            SetProgressDialog setProgressDialog = new SetProgressDialog(context);
            setProgressDialog.getAlertDialog("Error de Carga Ad", "Revisa tu conexión a internet.\n" +
                    "Intentalo más luego");
        }
    }


    @Override
    public void loadAd_Adwarded(Boolean isLoaded) {
        if(isLoaded) {
           // lyMoreYuans.setVisibility(VISIBLE);
            //progressBar.setVisibility(GONE);
        }else
        {
            //lyMoreYuans.setVisibility(GONE);
            //progressBar.setVisibility(VISIBLE);

           /* new CountDownTimer(30000, 5000) {
                public void onFinish() {
                    // When timer is finished
                    // Execute your code here
                    //se coloca un atarea final
                    dialog.dismiss();
                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                    //aqui se coloca el mensaje repetitivo o tarea repetitiva

                }
            }.start();*/
        }
    }

    @Override
    public void CloseAd_adwarded(Boolean wasClosed) {
        if (wasClosed) {
            lyMoreYuans.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
            dialog.dismiss();

        }
    }

    @Override
    public void OpenAd_adwarded(Boolean isOpened) {

    }

    @Override
    public void Rewarded_Permit(Boolean rewardedStatus, int ValRewarded) {
        giftYuanes(context, ValRewarded); //recibe el monto establecido en admod
        txtUpdateMonedas.setText(getYuanVal(context)+""); //actualiza el textview
    }

    @Override
    public void CloseAd_Intertisial(Boolean isCLosed) {

    }

    @Override
    public void FailAd_Intersticial(int NumbersTry) {

    }

    @Override
    public void preLoadAd_Intersticial(Boolean isPreloaded) {

    }
}

    //////////////////////////////////////////////////////////////////////////////////////