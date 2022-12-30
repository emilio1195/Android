package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import ec.blcode.stickerswapp.R;

public class SetProgressDialog {
    Context context;
    ProgressDialog progressUploading;
    private int increment = 0;

    public SetProgressDialog(Context context) {
        this.context = context;
    }

    public void simulateProgressDialog(String Titulo, String Mensaje){
        progressUploading = new ProgressDialog(context);
        progressUploading.setTitle(Titulo);
        progressUploading.setMessage(Mensaje);
        progressUploading.setIcon(R.mipmap.ic_launcher);
        progressUploading.setCancelable(false);
        progressUploading.setMax(100);
        progressUploading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressUploading.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressUploading.dismiss();
            }
        });
        progressUploading.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (progressUploading.getProgress() < progressUploading.getMax()) {
                        Thread.sleep(120);
                        progressUploading.incrementProgressBy(10);
                    }
                    progressUploading.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public ProgressDialog IncProgressDialog(String Titulo, String Mensaje){
        progressUploading = new ProgressDialog(context);
        progressUploading.setTitle(Titulo);
        progressUploading.setMessage(Mensaje);
        progressUploading.setIcon(R.mipmap.ic_launcher);
        progressUploading.setCancelable(false);
        progressUploading.setMax(100);
        progressUploading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressUploading.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressUploading.dismiss();
            }
        });
        return progressUploading;
    }

    public ProgressDialog getProgressDialog(String Titulo, String Mensaje){
        progressUploading = new ProgressDialog(context);
        progressUploading.setTitle(Titulo);
        progressUploading.setMessage(Mensaje);
        progressUploading.setIcon(R.mipmap.ic_launcher);
        progressUploading.setCancelable(false);
        progressUploading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressUploading.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressUploading.dismiss();
            }
        });
        return progressUploading;
    }

    public void getAlertDialog(String Titulo, String Mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Titulo);
        builder.setMessage(Mensaje);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}