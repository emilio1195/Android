package ec.blcode.stickerswapp.Functions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import ec.blcode.stickerswapp.R;

public class NewUserIntroActivity extends IntroActivity {
    private int numberPage;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setButtonBackVisible(false);
        addSlide(new SimpleSlide.Builder()
                .title("Bienvenido a Sticker Wapp EC!")
                .description("La nueva plataforma dinámica de Stickers, aquí encontrarás variedad y nuevo contenido cada día o semana.\n"
                +"Hemos implementado la logística de los videojuegos, donde te recompensan con monedas al ver anuncios." +
                        "\n\nCUENCA - ECUADOR")
                .image(R.drawable.logositckerswappec)
                .background(R.color.acent_DarkColor)
                .scrollable(false)
                .build());

       /* if(!checkIfBatteryOptimizationIgnored() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            addSlide(new SimpleSlide.Builder()
                    .title("We've recognized our app is optimized by Android's Doze system.")
                    .description("Para que la app funcione correctamente, porfavor deshabilita la optimización  para \"Sticker Wapp EC\" after clicking the button.")
                    .background(R.color.acent_DarkColor)
                    .scrollable(false)
                    .buttonCtaLabel("Let's Go")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                            startActivityForResult(intent, 4113);
                        }
                    })
                    .build());
        }*/




        setButtonBackVisible ( true );
        setButtonBackFunction (BUTTON_BACK_FUNCTION_BACK);
        addSlide(new SimpleSlide.Builder()
                .title("Publicidad Y Donaciones")
                .description("Se utiliza publicidad, con el fin de cubrir los costos que Google cobra por utilizar sus servicios.\n" +
                        "\nHay dos botones de publicidad:\n" +
                        "  - Boton de anuncios Cortos que recompensarán con " + getResources().getInteger(R.integer.Reward_Ad_Intersticial) + " Yuans.\n" +
                        "  - Boton de anuncios Largos que recompensarán con más Yuans.\n\n" +
                        "También hay un boton de donación, para apoyar el desarrollo del proyecto, de igual manera te recompensaré con monedas.")
                .background(R.color.purpura)
                .image(R.drawable.img_ads)
                .scrollable(false)
                .build());

        setButtonBackVisible ( true );
        setButtonBackFunction (BUTTON_BACK_FUNCTION_BACK);
        addSlide(new SimpleSlide.Builder()
                .title("Contenido de la App")
                .description("Para ver el contenido, deberás registrarte mediante el boton de inicio de sesión rápido, que he implementado.\n" +
                        "Una vez registrado, entrarás a la ventana donde encontrarás las categorías de los stickers.")
                .image(R.drawable.categorias)
                .background(R.color.anil)
                .scrollable(false)
                .build());

        setButtonBackVisible ( true );
        setButtonBackFunction (BUTTON_BACK_FUNCTION_BACK);
        addSlide(new SimpleSlide.Builder()
                .title("Stickers y Adquisición")
                .description("Cada categoría contiene sus respectivos stickers, cada día se subirá nuevo contenido." +
                        " No tendrás limites, puedes ingresar las veces que necesites para añadir stickers a Whats app.")
                .image(R.drawable.compra)
                .background(R.color.anil)
                .scrollable(false)
                .build());

        setButtonBackVisible ( true );
        setButtonBackFunction (BUTTON_BACK_FUNCTION_BACK);

        setButtonNextVisible ( true );
        setButtonNextFunction(BUTTON_NEXT_FUNCTION_NEXT_FINISH);

        setButtonNextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberPage == getSlides().size()-1)
                    otorgar_permisos();
                    //finish();
                else
                    nextSlide();
            }
        });
        /*addSlide(new SimpleSlide.Builder()
                .title("GRACIAS Y A DISFRUTAR")
                .description("Espero que te guste la app y que la puedas compartir, falta mucho por mejorar, pero cuando el equipo crezca la app crecerá, " +
                        "te pido de la manera mas amable, tu paciencia en la cantidad de contenido, ya que desarrollar la app y " +
                        "subir contenido requiere de más personas y tiempo.\n" +
                        "Espero tu apoyo, para que muchas personas que les gusta hacer contenido puedan trabajar en este nuevo proyecto." +
                        " Para dar una sugerencia, hay un boton de contacto en la sala principal.\n" +
                        "\nPara que la app funcione correctamente, por favor, acepta los permisos de escritura y lectura de datos, que son necesarios " +
                        "para la app.\n"+
                        "\nUN ABRAZO, Y QUE DISFRUTES LA APP AL MAXIMO!!")

                .background(R.color.vino)
                .scrollable(false)
                .build());
        */
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                numberPage = i;
                if(i>= 0 && i < getSlides().size()-1){
                    setNavigation(true, true);  //cuando estoy en diapositivas antes del tope puedo avanzar y retroceder

                }/* else if(i >= 0 && i <= 2) //para la intro pag1 y 2
                    setNavigation(true, false);  //cuando estoy en diapositivas antes del tope puedo avanzar y retroceder*/
                else{
                setNavigation(false, true);   //cuando llego al tope, ya no puedo seguir en foward
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    private  void   otorgar_permisos(){
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            verifyStoragePermissions(NewUserIntroActivity.this);

           /* setButtonBackVisible ( true );
            setButtonBackFunction (BUTTON_BACK_FUNCTION_BACK);
            addSlide(new SimpleSlide.Builder()
                    .title("Permisos")
                    .description("Para que la app funcione correctamente, porfavor, acepta los permisos de escritura y lectura de datos en tu memoria, además los de Internet.\n" +
                            "En el caso de no aparecer la ventana respectiva de solicitud, los permisos suelen aceptarse automáticamente, para evitar problemas de funcionamiento.")
                    .background(R.color.violeta)
                    .scrollable(false)
                    .buttonCtaLabel("Otorgar Permisos")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verifyStoragePermissions(NewUserIntroActivity.this);
                        }
                    })
                    .build());*/
        }else
            finish();
    }

    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4113){
            if(checkIfBatteryOptimizationIgnored()){
                setNavigation(true, false);
                nextSlide();
            }
        }
    }

    private boolean checkIfBatteryOptimizationIgnored(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(packageName);
        } else {
            return true;
        }
    }*/



    private void setNavigation(final boolean forward, final boolean backward){
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int i) {
                return forward;
            }

            @Override
            public boolean canGoBackward(int i) {
                return backward;
            }
        });
    }



    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Se solicita en array de permisos que desea q el usuario acepte
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                            //Manifest.permission.
                         },
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setPositiveButton("Let's Go", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    verifyStoragePermissions(NewUserIntroActivity.this);
                                }
                            })
                            .create();
                    alertDialog.setTitle("Notice!");
                    alertDialog.setMessage("Allowing storage permissions is crucial for the app to work. Please grant the permissions.");
                    alertDialog.show();
                } else {
                    setNavigation(true, false);
                    nextSlide();
                    finish();
                }
                break;
        }
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
            builder.setTitle("Boton Deshabilitado");
            builder.setMessage("No puedes salir mediante esta opción.");
            builder.setPositiveButton("Aceptar", null);
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
