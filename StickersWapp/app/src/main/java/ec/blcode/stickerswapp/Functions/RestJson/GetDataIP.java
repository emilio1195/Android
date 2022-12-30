package ec.blcode.stickerswapp.Functions.RestJson;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeDataAppText;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_IP;
import static ec.blcode.stickerswapp.Functions.Network_Conectivity_Status.isOnlineNet;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;

public class GetDataIP extends AsyncTask<String, String, String> {
    // declaramos dos variables para manejar el tiempo de conexión y un String para almacenar-mostrar los datos depues en el listview
    String pregrespcomment;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    //private String url_IP = "http://ip-api.com/json/?fields=status,continent,country,regionName,city,zip,lat,lon,isp,org,query";

    Context context;
    //ProgressDialog pdLoading;
    HttpURLConnection conn;
    URL url;

    public GetDataIP(Context context) throws IOException {
        this.context = context;
        //pdLoading = new ProgressDialog(context);
        url = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // pdLoading.setMessage("\tConsultando...");
        //pdLoading.setCancelable(false);
        //pdLoading.show();

    }
    @Override
    protected String doInBackground(String... params) {
        String publicIP = "";
        if (isOnlineNet()) {
            try {
                java.util.Scanner s = new java.util.Scanner(
                        new java.net.URL(
                                "https://api64.ipify.org/")
                                .openStream(), "UTF-8")
                        .useDelimiter("\\A");
                publicIP = s.next();
                //Log.d("IP>>",  publicIP);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return publicIP;

    }

    @Override
    protected void onPostExecute(String publicIP) {
        //Log.e("PublicIP", publicIP);
        //Here 'publicIp' is your desire public IP
        String Ipread = readDataAppText(context, KEY_IP);
        if (!publicIP.equals(Ipread)){
            if(!publicIP.equals("")){
                writeDataAppText(context, KEY_IP, publicIP);
                CloudFirebase cloudFirebase = new CloudFirebase(context);
                cloudFirebase.addDataIp(publicIP);
            }
        }
        //pdLoading.dismiss();

    }
}