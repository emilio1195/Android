package edu.uda.base_de_datos_completa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class consultar_dato extends Activity {
    EditText consulta1, consulta2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_dato);
        consulta1=(EditText) this.findViewById(R.id.dato1);
        consulta2=(EditText) this.findViewById(R.id.dato2);
    }

    public void IrConsultar(View v){
        Intent ventanaConsultar=new Intent(consultar_dato.this,eliminar_actualizar.class);

        ventanaConsultar.putExtra("datoConsult1",consulta1.getText().toString());
        ventanaConsultar.putExtra("datoConsult2",consulta2.getText().toString());
        startActivity(ventanaConsultar);
    }

}
