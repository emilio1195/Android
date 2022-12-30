package edu.uda.pets;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.Session;

import edu.uda.pets.JavaMail.DataAccess;
import edu.uda.pets.JavaMail.JavaMailAPI;

public class Contacto extends AppCompatActivity {
    EditText nombre, email, mensaje;
    Button enviar;
    String  destinatario, mensajeCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        nombre = (EditText) findViewById(R.id.edNombre);
        email = (EditText) findViewById(R.id.edCorreo);
        mensaje = (EditText) findViewById(R.id.edMensaje);
        enviar = (Button) findViewById(R.id.btEnviar);
        ///////////////////////////////////////////////
        enviar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  mensajeCorreo = "Usuario: " + nombre.getText().toString() + "\n"
                          + "Correo: " + email.getText().toString() + "\n"
                          + mensaje.getText().toString();

                  JavaMailAPI correo = new JavaMailAPI(Contacto.this, DataAccess.destinatario,
                          "Comentario", mensajeCorreo);

                  correo.execute();
              }
        });
        /////////////////////////// ////////////////////////

    }
}