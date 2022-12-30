package edu.uda.base_de_datos_completa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class eliminar_actualizar extends Activity {
    EditText txt1, txt2, txt3, txt4, txt5;
    DBAdapter db;
    int code=0;
    int telef=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_actualizar);
        txt1=(EditText) this.findViewById(R.id.editText1);
        txt2=(EditText) this.findViewById(R.id.editText2);
        txt3=(EditText) this.findViewById(R.id.editText3);
        txt4=(EditText) this.findViewById(R.id.editText4);
        txt5=(EditText) this.findViewById(R.id.editText5);
        String dato= getIntent().getStringExtra("datoConsult1");
        String dato2= getIntent().getStringExtra("datoConsult2");
        if (dato.isEmpty()){
            telef= Integer.parseInt(dato2);
        }
        else
        code= Integer.parseInt(dato);

        db = new DBAdapter(this);
        if(code>0){
            db.open();
            Cursor c = db.getContact(code);
            DisplayContact(c);
            db.close();
        }
        else{
            db.open();
            Cursor c = db.getDatoTelef(telef);
            DisplayContact(c);
            db.close();
        }
    }
    public void DisplayContact(Cursor c)
    {
        txt1.setText(c.getString(0));
        txt2.setText(c.getString(1));
        txt3.setText(c.getString(2));
        txt4.setText(c.getString(3));
        txt5.setText(c.getString(4));
    }

    public void Eliminar(View v) {
        if(code>0){
            db.open();
            db.deleteContact(code);
            db.close();
        }
        else{
            db.open();
            db.deleteDatoTelef(telef);
            db.close();
        }
        Toast.makeText(this, "Eliminado", Toast.LENGTH_LONG).show();
        finish();

    }

    public void actualizar(View v){
        int codig= Integer.parseInt(txt1.getText().toString());
        db.open();
        db.updateContact(codig,txt2.getText().toString(),txt3.getText().toString(),txt4.getText().toString(),txt5.getText().toString());
        db.close();
        Toast.makeText(this, "Actualizado", Toast.LENGTH_LONG).show();
        finish();
    }
}
