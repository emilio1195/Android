package edu.uda.serverWeb_Sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Consulta extends AppCompatActivity {
    public ArrayList<DatosBanda> bandaX = new ArrayList<DatosBanda>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        ActualizarAdaptador();
    }

    public void ActualizarAdaptador() {
        TareaWS obj1 = new TareaWS();
        try {
            bandaX.clear();
            String url = "http://10.0.2.2:9090/ServerWEb_MySQL/webresources/connom?Texto=2";
            Log.e("url nuevo", url);
            String respuestaactual = obj1.execute(url).get();
            JSONObject jsonObject = new JSONObject(respuestaactual);
            JSONArray arreglo = new JSONArray(jsonObject.getString("DatosBD"));
            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject objJson = arreglo.getJSONObject(i);
                bandaX.add(new DatosBanda(objJson.getString("codigo"), objJson.getString("nombre"),
                        objJson.getString("tipo")));
            }
            Lista fragmentoLista = (Lista) getFragmentManager().findFragmentById(R.id.fragDatos);
            Adaptador adapter = new Adaptador(getBaseContext(), bandaX);
            fragmentoLista.setListAdapter(adapter);
            fragmentoLista.ActualizarAdaptador(bandaX);
        } catch
        (Exception e) {
            e.printStackTrace();
        }
    }

    class TareaWS extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return consultarWS(urls[0]);
        }

        protected void onPostExecute(String result) {
        }

        public String consultarWS(String URL) {
            DefaultHttpClient client = new DefaultHttpClient();
            StringBuilder stringBuilder = new StringBuilder();
            HttpGet httpGet = new HttpGet(URL);
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } else {
                    Log.e("JSON", "Error en la descarga ...");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
    }

}

