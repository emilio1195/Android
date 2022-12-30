package ec.blcode.stickerswapp.MainActivities;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.payYuan;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_EMAIL;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_ENTRY_SUPPORT;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_NAME_LASTN;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_UUID;
import static ec.blcode.stickerswapp.Functions.Dialogos.CuadroDialogDonate.setDialogDonate;
import static ec.blcode.stickerswapp.Functions.Network_Conectivity_Status.isOnlineNet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import ec.blcode.stickerswapp.Adapter.ViewPageAdapter;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.StickerBook;
import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.Functions.Admod.Admod;
import ec.blcode.stickerswapp.Functions.BillingGoogle.BillingCLient_Google;
import ec.blcode.stickerswapp.Functions.Dialogos.DialogCompartirApp;
import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.Functions.RestJson.GetDataIP;
import ec.blcode.stickerswapp.POJO.DataUser;
import ec.blcode.stickerswapp.R;
import ec.blcode.stickerswapp.StickersListCartoon_Fragment;
import ec.blcode.stickerswapp.View.ViewAddStickerDB.View_Upload_Stickers;
import ec.blcode.stickerswapp.View.ViewPackListStatics.View_ListPacks_Fragment;

public class MainScreenStickers extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //FloatingActionButton fbtnAddStiker;
    Admod admod;
    BillingCLient_Google billingCLient_google;
    private SetProgressDialog setProgressDialog;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;

    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_stickers);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vPager);
       // fbtnAddStiker = (FloatingActionButton) findViewById(R.id.fbtnAddSticker);
      //  fbtnAddStiker.setVisibility(View.VISIBLE);
        ///////////////////////////Iniciar Banner/////////////////////////////////////////////////////
        admod = new Admod(this);
        admod.initBannerAdmod();
        ////////////////////////////////////////////////////////////////////////////////////////////
        billingCLient_google = new BillingCLient_Google(this);
        billingCLient_google.initBillingClient();
        ///////////////////////////////////////////////////////////
        //Se obtiene un obj con un directorio que pertenece a una direccion privada, que solo la app puede acceder a ella
        //ademas se incrementa la carpeta assets, donde se guardaran los archivos .json de los packs y los stickers
        String path = getFilesDir() + "/";
        StickerBook.init(this);

        Bundle bundle = new Bundle();
        bundle.putString("Login","Ha hecho Login Ahora esta en pantalla principal de stickers");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        getUserinfo();

        initRater();

        // Llamo al método para recibir la data IP
        try {
            if(isOnlineNet())
                  new GetDataIP(this).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SetUpViewPager();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

       /* fbtnAddStiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });*/
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Salir");
            builder.setMessage("Seguro quieres salir de app?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if (isOnlineNet())
          //  billingCLient_google.verificarComprasPendientes();
        show_Instruction_Buttons();


    }

    @Override
    protected void onStop() {
        super.onStop();
       // if(readBooleanData(this, KEY_CLIC_COMPARTIR))
         //  writeBooleanData(this, KEY_OTORGAR, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mDonar:
                setDialogDonate(this);
                break;
            case R.id.mAbout:
                contactar();
                break;
            case R.id.mRating:
                showRateApp();
                break;
            case R.id.mSaveMoneyStickers:
                SetProgressDialog setProgressDialog = new SetProgressDialog(this);
                setProgressDialog.getAlertDialog("Guardar Monedas & Stickers",
                        "Esta función estará lista próximamente.\n" + "Disculpa las molestias.");
                break;
            case R.id.mManualpdf:
                startActivity(new Intent(this, ManualEliminarSt.class));
                break;

            case R.id.mCompartir:
                new DialogCompartirApp(this);
                break;
            case R.id.mSupportApp:
                    Dialog_Entry_Support_Stickers();
                break;
            case R.id.mYuans:
                RecibirYuans();
                break;
            case R.id.mSalir:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private void RecibirYuans() {
        //este metodo sirve para recuperar monedas que la app no pudo procesar y asi cubrir la recompensa de un usuario al donar
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recibir Monedas Por Donar");
        builder.setMessage("Si realizaste una donación y han pasado varios días que no has recibido tus Yuans, debes presionar el icono " +
                "de información, donde debes enviarnos un Email, adjuntando tu Nombre y Apellido, tu 'Token de compra' que te llegó al correo o puedes enviarnos " +
                "una captura de tu factura de compra que te llega al correo, y tu correo con el que te registraste. \n\n" +
                "Si ya te contactaste con nosotros y recibiste la confirmación de recibir tus Yuans da clic en Recibir, caso contrario " +
                "da Clic en Salir.");
        builder.setPositiveButton("Recibir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isOnlineNet()){
                    CloudFirebase cloudFirebase = new CloudFirebase(MainScreenStickers.this);
                    cloudFirebase.getDataDonor(readDataAppText(MainScreenStickers.this, KEY_UUID),
                            readDataAppText(MainScreenStickers.this, KEY_NAME_LASTN));
                }else
                    Toast.makeText(MainScreenStickers.this, "Debes estar Conectado a Internet", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Salir", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void signOut() {
        if(isOnlineNet()) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Log-in");
            FirebaseMessaging.getInstance().subscribeToTopic("Log-out");
            FirebaseAuth.getInstance().signOut();
        }
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        this.finish();
    }

    private ArrayList<Fragment> AddFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new View_ListPacks_Fragment());
        fragments.add(new StickersListCartoon_Fragment());
       // fragments.add(new MisStickers_Fragment());
        return fragments;
    }

    private void SetUpViewPager() {
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), AddFragments()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Estaticos");
        tabLayout.getTabAt(1).setText("Animados");
       // tabLayout.getTabAt(2).setText("Mis Stickers");
    }

    public void getUserinfo() {
        Bundle getInforUser = getIntent().getExtras();
        DataUser dataUser = null;
        String uuid = readDataAppText(this, KEY_UUID);

        if (getInforUser != null) {
            dataUser = (DataUser) getInforUser.getSerializable("InforUser");
            Log.d("DataUser>>", dataUser.getName_LastN() + ", " + dataUser.getBirth_Date()
                    + ", " + dataUser.getGender());
            if (!uuid.equals(dataUser.getUuid()) || uuid.equals("")) {
                writeDataAppText(this, KEY_UUID, dataUser.getUuid());
                writeDataAppText(this, KEY_NAME_LASTN, dataUser.getName_LastN());
                writeDataAppText(this, KEY_EMAIL, dataUser.getEmail());
            } else
                FirebaseCrashlytics.getInstance().setUserId(uuid);
        }else
            FirebaseCrashlytics.getInstance().setUserId(uuid);
    }
///////////////////////////////////////////Dialog for Entry to Add Stickers /////////////////////////////////
    private void Dialog_Entry_Support_Stickers() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Personal Autorizado");
        dialog.setMessage("Porfavor, ingresa el código de autorización para poder añadir Stickers.\nSolo el personal de soporte dispone este codigo.");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText codeBox = new EditText(this);
        codeBox.setLines(1);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(50, 0, 50, 10);
        codeBox.setLayoutParams(buttonLayoutParams);
        codeBox.setHint("<<<<<< CODIGO >>>>>>");
        codeBox.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        layout.addView(codeBox);

        final EditText creatorBox = new EditText(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            creatorBox.setAutofillHints("code");
        }

        dialog.setView(layout);

        dialog.setPositiveButton("OK", null);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog ad = dialog.create();

        ad.show();

        final Button b = ad.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Editable codigo = codeBox.getText();
                if (TextUtils.isEmpty(codigo) && !readBooleanData(MainScreenStickers.this, KEY_ENTRY_SUPPORT)) {
                    codeBox.setError("Se requiere código");
                } else {
                    ad.dismiss();
                    if (!readBooleanData(MainScreenStickers.this, KEY_ENTRY_SUPPORT))
                        if (codigo.toString().equals("ANKAEDY-2020-NOVIEMBRE-28-4N4D1R-5T1K3R")) {
                            writeBooleanData(MainScreenStickers.this, KEY_ENTRY_SUPPORT, true);
                            Intent goScreenAdd = new Intent(MainScreenStickers.this, View_Upload_Stickers.class);
                            startActivity(goScreenAdd);
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.toast_custom_error, (ViewGroup) findViewById(R.id.toast_custom_Error));
                            TextView text = (TextView) layout.findViewById(R.id.text);
                            text.setText("CODIGO INCORRECTO!");

                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    else {
                        Intent goScreenAdd = new Intent(MainScreenStickers.this, View_Upload_Stickers.class);
                        startActivity(goScreenAdd);
                    }
                }
            }
        });

        creatorBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    b.performClick();
                }
                return false;
            }
        });
    }

    //////////////////////////RECOVERY MONEY & STICKERS USER //////////////////////////////////////////////
    private void UploadYuansStickers(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Uploaded Data");
        builder.setMessage("Lamentablemente Google cobra por sus servicios de respaldo en la nube, por ello, " +
                "Subir a la nube tus datos te valdran 700 Yuans." +
                "\nPor esta razón, respalda tus monedas y stickers cuando sea necesario.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                paySticker(500);
                // Toast.makeText(MainScreenStickers.this, "No Disponible. Estamos trabajando en esto. Lo sentimos.", Toast.LENGTH_LONG).show();
            }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("Cancelar", null);
        dialog = builder.create();
        dialog.show();
    }

    //////////////////////////////////////PAGO PARA RESPALDO///////////////////////////////////////////
    private void paySticker(int costo) {
        Boolean paid = payYuan(costo, this); //si el valor del precio es menor al salario, se procede el pago y retorna true
        if (paid) {
            progressDialog = setProgressDialog.getProgressDialog("Uploading....",
                    "Espera un momento, mientras se suben tus datos a la nube.");
            progressDialog.show();
            String uidUser = readDataAppText(this, KEY_UUID);
            ///AQUI CODE FOR RECOVERY


        } else {
            dialog.dismiss();
            setProgressDialog = new SetProgressDialog(this);
            setProgressDialog.getAlertDialog("FAIL UPLOAD", "La cantidad de monedas es insuficiente, para realizar el respaldo de tus datos." +
                    "\n Gana monedas >> viendo anuncios o donando a la app.");
        }

    }
    /////////////////////////////////////////Lectura Json IP DAta///////////////////////////////////////////////////////////////////////////////////////}

    private void contactar(){
        Intent enviarEmail =  new Intent(Intent.ACTION_SEND);
        enviarEmail.setType("text/html");
        enviarEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"ankaedy.ec@gmail.com"});
        enviarEmail.putExtra(Intent.EXTRA_TITLE, "SUGERENCIA");
        enviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Sugerencia o Reportar un Problema.");
        try{
            startActivity(Intent.createChooser(enviarEmail, "Seleccionar un Proveedor de Email..."));
        }catch (ActivityNotFoundException activityNotFoundException){
            Toast.makeText(this, "No se ha encontrado ninguna app de mensajería electrónica.", Toast.LENGTH_LONG).show();
        }
    }

    ////////////////////////////////////////CODIGO PARA MOSTRAR EL DIALOGO DEL CALIFICADOR DE LA APP
    private void initRater() {
        //reviewManager = new FakeReviewManager(this);  //es una API de simulacion  para comprobar el funcionamiento
        reviewManager = ReviewManagerFactory.create(this);  //Esta funcion es la original
            //boton para calificar manualmente
        //findViewById(R.id.btn_rate_app).setOnClickListener(view -> showRateApp());
    }

    /**
     * Shows rate app bottom sheet using In-App review API
     * The bottom sheet might or might not shown depending on the Quotas and limitations
     * https://developer.android.com/guide/playcore/in-app-review#quotas
     * We show fallback dialog if there is any error
     */
    public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, continue regardless of the result.
                // show native rate app dialog on error
                showRateAppFallbackDialog();
            }
        });
    }

    /**
     * Showing native dialog with three buttons to review the app
     * Redirect user to playstore to review the app
     */
    private void showRateAppFallbackDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.rate_app_title)
                .setMessage(R.string.rate_app_message)
                .setPositiveButton(R.string.rate_btn_pos, (dialog, which) -> {
                    redirectToPlayStore();
                })
                .setNegativeButton(R.string.rate_btn_neg,
                        (dialog, which) -> {
                        })
                .setNeutralButton(R.string.rate_btn_nut,
                        (dialog, which) -> {
                        })
                .setOnDismissListener(dialog -> {
                })
                .show();
    }

    // redirecting user to PlayStore
    public void redirectToPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == CODE_SHARE) {
            if(data != null)
                Log.i("data_share", "Intent data>> " + data.toString());
            Log.i("data_share", "Codes>> "+ requestCode + ", " + resultCode);
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"Cancel Share",Toast.LENGTH_LONG).show();
            }else {
                int cantidadCompartir = readYuanes(this, KEY_NUMBER_COMPARTIR);

                if(readBooleanData(this, KEY_CLIC_COMPARTIR)){
                    cantidadCompartir++;
                    writeYuanes(this, KEY_NUMBER_COMPARTIR, cantidadCompartir);
                   // writeBooleanData(this, KEY_OTORGAR, false);
                    writeBooleanData(this, KEY_CLIC_COMPARTIR, false);
                    giftYuanes(this,100);
                    putAlertDialog(this);
                    //Toast.makeText(this, "OK SHARE", Toast.LENGTH_LONG).show();
                }
            }
        }*/
    }


    private void show_Instruction_Buttons(){
        new MaterialIntroView.Builder(MainScreenStickers.this)
                .enableIcon(false) // Desactiva el icono de ayuda, el valor predeterminado es verdadero
                .setFocusGravity(FocusGravity.RIGHT)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(2000)  //tiempo en el que se demora en aparecer
                .enableFadeAnimation(true) // Muestra el centro de animación de puntos del área de enfoque
                .performClick(true)  // Activa la operación de clic cuando el usuario hace clic en el área de enfoque.
                .setInfoText("Aquí encontrarás: Donar, Contactar, Calificar App, Menu, respectivamente. \n" +
                        "Presiona cualquiera, para salir de la Intro")
                .setShape(ShapeType.RECTANGLE)
                .setTarget(toolbar)
                .setUsageId("intro_Toolbar") //THIS SHOULD BE UNIQUE ID
                .setMaskColor(getResources().getColor(R.color.blanco_alpha)) //pinta toda la pantalla para enfocar al boton
                // Permitir que esta superposición de presentación solo se muestre una vez. Evita que se muestren varias pantallas al mismo tiempo.
                // Útil si desea mostrar un paso de recorrido en un código que se llama varias veces
                .setIdempotent(true)
                /*.setListener ( new  MaterialIntroListener() {
                    @Override
                    public  void  onUserClicked ( String  materialIntroViewId ) {

                    }
                })*/
                .show();
    }

}