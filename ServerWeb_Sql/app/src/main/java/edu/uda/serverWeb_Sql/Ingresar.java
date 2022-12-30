package edu.uda.serverWeb_Sql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class Ingresar extends AppCompatActivity {
    EditText nombre;
    EditText genero;
    EditText codigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        nombre = (EditText) findViewById(R.id.nom);
        genero =(EditText) findViewById(R.id.gener) ;
        codigo= (EditText) findViewById(R.id.cod);
        codigo.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
    public void InsertarDatos(View view) {
        String n=nombre.getText().toString();
        String t= genero.getText().toString();
        String c=codigo.getText().toString();
        String link= "http://10.0.2.2:9090/ServerWEb_MySQL/webresources/connom?Texto=1"
                                                            +";'"+c+"','"+n+"','"+t+"'";
        new  ReadJSONFeedTask().execute(link);
        nombre.setText("");
        genero.setText("");
        codigo.setText("");

        Toast.makeText(this, "OK INSERT", Toast.LENGTH_LONG).show();
    }


    public String readJSONFeed(String URL)
    {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try
        {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }
            }
            else
            {
                Log.e("JSON", "Failed to download file");
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    private class ReadJSONFeedTask extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls)
        {
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result)
        {
            try

            {
//JSONObject jsonobjest = new JSONObject(result);


                JSONObject jsonObject = new JSONObject(result);
                //Toast.makeText(getBaseContext(), jsonObject.getString("resultado"), Toast.LENGTH_LONG).show();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

