package ec.blcode.stickerswapp.MainActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ec.blcode.stickerswapp.R;

import static ec.blcode.stickerswapp.Functions.Network_Conectivity_Status.isOnlineNet;

public class ManualEliminarSt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_eliminar_st);
        Button btnVerPdf = (Button) findViewById(R.id.btnPdf);

        btnVerPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url =  "https://firebasestorage.googleapis.com/v0/b/stickers-wapp.appspot.com/o/Manual_pdf%2FEliminar%20Packs%20Stickers%20Wapp-signed.pdf?alt=media&token=a899652a-1d99-4c6a-b158-c70c7424b6a7";
                //openPdfManual(ManualEliminarSt.this);
                if(isOnlineNet()){
                    Intent intent = new Intent();
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    startActivity(intent);
                }else
                    Toast.makeText(ManualEliminarSt.this, "Debe tener conexión a internet, para poder abrir el archivo.", Toast.LENGTH_LONG).show();

            }
        });
    }
}