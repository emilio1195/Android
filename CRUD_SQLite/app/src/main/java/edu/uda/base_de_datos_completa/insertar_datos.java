package edu.uda.base_de_datos_completa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class insertar_datos extends Activity {
    EditText txt1, txt2, txt3, txt4, txt5;
    DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_datos);
        db = new DBAdapter(this);
        txt1=(EditText) this.findViewById(R.id.dato1);
        txt2=(EditText) this.findViewById(R.id.dato2);
        txt3=(EditText) this.findViewById(R.id.dato3);
        txt4=(EditText) this.findViewById(R.id.dato4);
        txt5=(EditText) this.findViewById(R.id.dato5);
        txt1.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt2.setInputType(InputType.TYPE_CLASS_TEXT);
        txt3.setInputType(InputType.TYPE_CLASS_TEXT);
        txt4.setInputType(InputType.TYPE_CLASS_NUMBER);
        txt5.setInputType(InputType.TYPE_CLASS_NUMBER);
        /////////////////////////////////////////////
        /////////////////////////////////////////////
        db = new DBAdapter(this);
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
    }
    public void insertar(View v){

        db.open();
        db.insertContact(txt1.getText().toString(),txt2.getText().toString(),txt3.getText().toString(),txt4.getText().toString(),txt5.getText().toString());
        db.close();
        Toast.makeText(this, "Insertado", Toast.LENGTH_LONG).show();
        finish();
    }
}
