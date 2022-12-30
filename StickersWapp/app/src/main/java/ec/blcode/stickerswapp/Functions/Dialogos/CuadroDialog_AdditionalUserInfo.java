package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ec.blcode.stickerswapp.POJO.DataUser;
import ec.blcode.stickerswapp.R;

public class CuadroDialog_AdditionalUserInfo implements AdapterView.OnItemSelectedListener {
    Context context;
    final Dialog dialog;
    EditText edDate, edDateM, edDateY;
    Spinner spGender;
    Button btnGuardar;
    ArrayList<String> ListGeneros = new ArrayList<>(Arrays.asList("Seleccione", "Hombre", "Mujer", "Otro"));
    private String Gender = "";
    private String BirthDate = "";
    private Interfaz_Additional_Info interfaz_additional_info;

    public CuadroDialog_AdditionalUserInfo(final Context context, Interfaz_Additional_Info viewInterfaz, final DataUser dataUser) {
        this.interfaz_additional_info = viewInterfaz;
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(false); //para que no sea cancelable al tocar fuera de la pantalla
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_additional_info_user);
        edDate = (EditText) dialog.findViewById(R.id.edBirthDate);
        spGender = (Spinner) dialog.findViewById(R.id.spGender);
        btnGuardar = (Button) dialog.findViewById(R.id.btnGuardar);
        ClicSpinnerGeneros();

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK
                        ,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar calendarResult =  Calendar.getInstance();
                        calendarResult.set(Calendar.YEAR, year);
                        calendarResult.set(Calendar.MONTH, month);
                        calendarResult.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date date = calendarResult.getTime();
                            BirthDate = simpleDateFormat.format(date); //me entrega la fecha seleccionada en String
                            edDate.setText(BirthDate);
                    }
                },c.get(Calendar.YEAR) - 18, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!BirthDate.equals("") && !Gender.equals("")) {
                    //enviar datos a la actividad
                    //Toast.makeText(context,"Data OK \n" + Gender + ", " + BirthDate,Toast.LENGTH_SHORT).show();
                    dataUser.setBirth_Date(BirthDate);
                    dataUser.setGender(Gender);
                    interfaz_additional_info.aditionalInfoUser(dataUser);
                    dialog.dismiss();
                }else
                    Toast.makeText(context,"Porfavor Completa los campos requeridos",Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    private void ClicSpinnerGeneros() {
        spGender.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_own, ListGeneros);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spGender:
                String gender = adapterView.getSelectedItem().toString();
                if (!gender.equals("Seleccione")) {
                    Gender = gender;
                }else
                    Gender = "";
                //Toast.makeText(this,adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}