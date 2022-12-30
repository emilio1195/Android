package edu.uda.base_de_datos_completa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
    TextView consulta;
    String concatenar="";
    DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        consulta=(TextView) this.findViewById(R.id.consultaGeneral);

        /////////////////////////////////////////////
        db = new DBAdapter(this);
        /////////////////////////////////////////////
        try
        {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();
//---copy the db from the assets folder into the databases folder---
//CopyDB(getBaseContext().getAssets().open("mydb"), new
                //FileOutputStream(destPath + "/mydb"));
            }
            String destArc = destPath+"/BandasMusica";
            Log.d("Ruta base",destArc);
            File basedatos = new File(destArc);
            if (!basedatos.exists())
                CopyDB(getBaseContext().getAssets().open("BandasMusica"), new
                        FileOutputStream(destPath + "/BandasMusica"));
        }
        catch (
                FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (
                IOException e)
        {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////
    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException
    {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public void Concatenar(Cursor c)
    {

        concatenar= "Codigo: " + c.getString(0) + "\n" + "Nombre: " + c.getString(1) + "\n"
                + "Genero: " + c.getString(2) + "\n"+ "Año: " + c.getString(3) + "\n"+ "Telefono: " + c.getString(4) + "\n" + concatenar;
    }



    public void ConsultGeneral(View v){
        consulta.setText("");
        concatenar="";
        db.open();
        Cursor c = db.getAllContacts();
        if (c.moveToFirst())
        {
            do
            {
                Concatenar(c);
            } while (c.moveToNext());
        }
        db.close();

        consulta.setText(concatenar);
    }
    public void IrConsultar(View v){
        Intent ir_consultar= new Intent(MainActivity.this, consultar_dato.class);
        startActivity(ir_consultar);
    }
    public void IrInsertar(View v){
        Intent ir_insertar= new Intent(MainActivity.this, insertar_datos.class);
        startActivity(ir_insertar);
    }
}
