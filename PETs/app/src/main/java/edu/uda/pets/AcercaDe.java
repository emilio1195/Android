package edu.uda.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AcercaDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        TextView txtabout = (TextView) findViewById(R.id.txtAbout);
        txtabout.setText("Hello, I have programmed since I was 13 years old, " +
                "knowing several languages, IDEs and Frameworks, throughout my learning and specialization " +
                "I obtained the ability to learn quickly and be self-taught, so with the training respective " +
                "I can learned a new language or area without problems.");
    }
}
