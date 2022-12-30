 package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ec.blcode.stickerswapp.R;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readYuanes;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeYuanes;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.giftYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_CLIC_COMPARTIR;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_DATE_TODAY;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_NUMBER_COMPARTIR;

public class DialogCompartirApp extends DialogFragment {
    public static final int CODE_SHARE = 9663;
    private int cantidadCompartir;
    Button btnCompartirApp;
    public DialogCompartirApp(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_dialog_compartir);
        TextView txtShowNumCompartir = (TextView) dialog.findViewById(R.id.txtCantidadCompartir);
        ProgressBar pbwait = (ProgressBar) dialog.findViewById(R.id.pbwaitShare);
        btnCompartirApp = (Button) dialog.findViewById(R.id.btnCompartirApp);
        cantidadCompartir = readYuanes(context, KEY_NUMBER_COMPARTIR);
        readDateToday(context);
        txtShowNumCompartir.setText(cantidadCompartir+"" + "/2");

        btnCompartirApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbwait.setVisibility(View.VISIBLE);
                CompartirApp(context);
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void CompartirApp(Context context) {
        String invitationLink = "https://stickerswapp.page.link/descargarApp";
        String Title = "Stickers Wapp EC";
        String msg = "Esta muy divertido su contenido, encontrarás variedad de stickers organizados por categorias.\n" +
                "Recibe 400 monedas al registrarte. Descárgala solo en Play Store con el siguiente Link: \n"
                + invitationLink;
        try{
            Intent compartir = new Intent(Intent.ACTION_SEND);
            compartir.setType("text/plain");
            compartir.putExtra(Intent.EXTRA_TITLE, "DESCARGA STICKERS WAPP EC");
            compartir.putExtra(Intent.EXTRA_SUBJECT, Title);
            compartir.putExtra(Intent.EXTRA_TEXT, msg);
            //Activity activity = (Activity) context;
            //activity.startActivityForResult(compartir, CODE_SHARE);
            CreateChooserInstalledApps(context, compartir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<ResolveInfo> AppsShare = null;

    private void CreateChooserInstalledApps(Context context , Intent shareIntent)
            throws PackageManager.NameNotFoundException {

        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        AppsShare = new ArrayList<>();
        List<DialogItem> appNames = new ArrayList<>();
        for (ResolveInfo info : activities) {

            //info.activityInfo.packageName;   //esta opcion entrega solo el paquete  com.whatsapp
            //info.loadLabel(context.getPackageManager()).toString().toLowerCase();
            String NameApp = info.activityInfo.packageName.toLowerCase();   //esta opcion entrega solo el paquete  com.whatsapp

            if(NameApp.contains("twitter") || NameApp.contains("whatsapp") || NameApp.contains("whats_app") ||
                    NameApp.contains("messenger") || NameApp.contains("facebook")) {
                appNames.add(0, new DialogItem(info.loadLabel(context.getPackageManager()).toString().toUpperCase(),
                        info.loadIcon(context.getPackageManager())));
                AppsShare.add(0, info);
            }

        }

        final List<DialogItem> newItem = appNames;
        //CREA EL ADAPATADOR CON EL ICNO Y EL TEXTO A LADO
        ListAdapter adapter = new ArrayAdapter<DialogItem>(context,
                android.R.layout.select_dialog_item, android.R.id.text1, newItem) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setText(newItem.get(position).app);
                tv.setTextSize(15.0f);
                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(newItem.get(position).icon, null, null, null);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * context.getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };
        //COLOCAMOR EL ADAPTADOR Y MOSTRAMOS EL CHOSER
        showAlerDialog(context, adapter, shareIntent);

    }

    private void showAlerDialog(Context context, ListAdapter adapter, Intent share) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("COMPARTIR CON: ");
        builder.setCancelable(false);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ResolveInfo info = AppsShare.get(item);

                if (!info.activityInfo.packageName.equals("")) {
                    // start the selected activity
                    Log.i("StactActivity", "Hi..hello. Intent is selected");
                    share.setPackage(info.activityInfo.packageName);
                    context.startActivity(share);
                    cantidadCompartir++;
                    if (cantidadCompartir <= 2) {
                        //se enlaza con el metodo onStop del MainScreenStickers
                        writeBooleanData(context, KEY_CLIC_COMPARTIR, true);
                        TemporizadorForRecompensar(context);
                    }
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void putAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("RECOMPENSA");
        builder.setMessage(" \uD83C\uDF89 Gracias por compartir \uD83C\uDF89 , has obtenido tu recompensa:\n" +
                " -> 100 Yuans ☺️");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    int mYear;
    int mMonth;
    int mDay;
    public void readDateToday(Context context) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        StringBuilder date = new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-")
                .append(mDay).append("-")
                .append(mYear).append(" ");
        String dateYestarday = readDataAppText(context, KEY_DATE_TODAY);
        if (!dateYestarday.equals(date.toString())){
            btnCompartirApp.setEnabled(true);
            writeDataAppText(context, KEY_DATE_TODAY,date.toString());
            cantidadCompartir = 0;
            writeYuanes(context, KEY_NUMBER_COMPARTIR, cantidadCompartir);
            //writeBooleanData(context, KEY_COMPLETE_COMPARTIR, false);

        }else{
            if(cantidadCompartir == 2) {
               // writeBooleanData(context, KEY_COMPLETE_COMPARTIR, true);
                btnCompartirApp.setEnabled(false);
            }
        }


    }

    private void TemporizadorForRecompensar(Context context){
        //verificamos si en un minuto el usuario completo la tarea de salirse de la App para compartir
        new CountDownTimer(20000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(readBooleanData(context, KEY_CLIC_COMPARTIR)){
                    cantidadCompartir++;
                    writeYuanes(context, KEY_NUMBER_COMPARTIR, cantidadCompartir);
                    // writeBooleanData(this, KEY_OTORGAR, false);
                    writeBooleanData(context, KEY_CLIC_COMPARTIR, false);

                    giftYuanes(context,100);
                    putAlertDialog(context);
                    //Toast.makeText(this, "OK SHARE", Toast.LENGTH_LONG).show();
                }

            }

        }.start();
    }

}
