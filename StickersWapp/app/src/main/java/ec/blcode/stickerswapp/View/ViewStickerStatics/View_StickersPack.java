package ec.blcode.stickerswapp.View.ViewStickerStatics;

import static ec.blcode.stickerswapp.CodeAddSticker2Wapp.StickerBook.allStickerPacks;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeStickerPurchased;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.getYuanVal;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.giftYuanes;
import static ec.blcode.stickerswapp.Functions.Books.PackBook.getDataPacksList;
import static ec.blcode.stickerswapp.Functions.Books.StickersPurchasedBook.getListStickersScript;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_ENTRY_SUPPORT;
import static ec.blcode.stickerswapp.Functions.Dialogos.CuadroDialogoBuy.ADD_PACK;
import static ec.blcode.stickerswapp.Functions.Network_Conectivity_Status.isOnlineNet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;
import ec.blcode.stickerswapp.Adapter.AdapterListaStickers;
import ec.blcode.stickerswapp.BuildConfig;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.WhatsAppBasedCode.BaseActivity;
import ec.blcode.stickerswapp.Functions.Admod.Admod;
import ec.blcode.stickerswapp.Functions.Admod.InterfazAdmod;
import ec.blcode.stickerswapp.Functions.Archivers.InterfazOpYuan;
import ec.blcode.stickerswapp.Functions.Dialogos.CuadroDialogoRecompensa;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.Presenter.PresenterStickerStatics.InterfacePresenterStickerStatics;
import ec.blcode.stickerswapp.Presenter.PresenterStickerStatics.Presenter_StickersListStatics;
import ec.blcode.stickerswapp.R;

public class View_StickersPack extends AppCompatActivity implements Interface_View_DetailsPack, InterfazOpYuan, InterfazAdmod {

    private  String ID_ADMOD_INTERSTICIAL_STICKERS;
    private  String ID_ADMOD_REWARDED_STICKERS_1;
    private RecyclerView recyclerView;
    TextView txtTitulo, txtMonedas;
    Button btnRewarded, btnIntersticial;
    GridLayoutManager gridLayoutManager;
    AdapterListaStickers adapterListaStickers;
    private InterfacePresenterStickerStatics IFPresenterStickersStatics;
    private AdView mAdView;
    private RewardedAd rewardedAd_Main;
    CuadroDialogoRecompensa cuadroDialogoRecompensa;
    RewardedAdLoadCallback adLoadCallback;
    ProgressBar progressBarRewardedButton, progressBarIntersticialButton;
    Admod admod;
    private int NumbersTryIntersticial = 0;
    private Boolean StatusAdwarded = true;
    private boolean Conectividad = true;
    private int numColumns;
   // private int Rewarded;
    ShimmerFrameLayout container;

    //int cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__stickers_pack);
        ///////////////////////////////////////////////////////////////////////////////////
        ID_ADMOD_REWARDED_STICKERS_1 = getString(R.string.admob_ad_Rewarded_test);
        ID_ADMOD_INTERSTICIAL_STICKERS = getString(R.string.admob_ad_Intersticial_Test);
        /////////////////////////////////////////////////////////////////////////////////////////

        recyclerView = (RecyclerView) findViewById(R.id.rvViewStickers);
        recyclerView.setHasFixedSize(true);

        txtTitulo = (TextView) findViewById(R.id.txtTituloPack);
        txtMonedas = (TextView) findViewById(R.id.txtMonedas);
        btnRewarded = (Button) findViewById(R.id.btnMonedas);
        btnIntersticial = (Button) findViewById(R.id.btnIntesticial);
        progressBarRewardedButton = (ProgressBar) findViewById(R.id.pbButtonRewardedAd);
        progressBarIntersticialButton = (ProgressBar) findViewById(R.id.pbButtonIntertisialAd);
        container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_StickersPreview);
        container.startShimmer(); // If auto-start is set to false

        String data = getIntent().getStringExtra("CategoriaPack");
        txtTitulo.setText( "Categoria >>> " + data);
        txtMonedas.setText(getYuanVal(this)+"");
        IFPresenterStickersStatics = new Presenter_StickersListStatics(this, data);
        admod = new Admod(this, this);
        ///////////////////////////Iniciar Banner/////////////////////////////////////////////////////
        admod.initBannerAdmod();
        ////////////////////////////////////Set BANNER///////////////////////////////////////////////////////////
        admod.setBannerAdmod(R.id.adViewBannerSecond);
        /////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////PRELOAD Rewarded MAIN//////////////////////////////////////////////////////////////////
        admod.initREwardedAd(ID_ADMOD_REWARDED_STICKERS_1);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////PRELOAD Intersticial //////////////////////////////////////////////////////////////////
        admod.initIntertisialAd(ID_ADMOD_INTERSTICIAL_STICKERS);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////OCULTA BOTONES DE ADS SI NO HAY INTERNET /////////////////////////////
        if (!isOnlineNet()){
            btnIntersticial.setVisibility(View.GONE);
            btnRewarded.setVisibility(View.GONE);
            progressBarIntersticialButton.setVisibility(View.VISIBLE);
            progressBarRewardedButton.setVisibility(View.VISIBLE);
            Conectividad = false;
        }
        ////////////////////////////////////
        btnRewarded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAdclic(View_StickersPack.this);

            }
        });

        btnIntersticial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnlineNet())
                 admod.LoadIntersticial_clic(NumbersTryIntersticial);
                else
                    Toast.makeText(View_StickersPack.this, "SIN CONEXION A INTERNET",Toast.LENGTH_SHORT).show();

            }
        });

       // Log.e("netHabilitada", Boolean.toString(isNetDisponible(this)));
        //Log.e("accInternet",   Boolean.toString(isOnlineNet()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //codigo adicional
        allStickerPacks = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    private void ShowAdclic (final Activity activity){
        admod.setRewardedAd();
    }
    //////////////////////////////////////////////////////////////////////////////////////

    private final ViewTreeObserver.OnGlobalLayoutListener pageLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int ancho = recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.dimen_cardview_Sticker_preview);
            int padding = (recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.padding));
            int anchoScreen = recyclerView.getWidth() - padding;
            int numColums = anchoScreen / ancho;
            int anchosStickers = ancho * numColums;
            if (anchosStickers < anchoScreen) {
                float paddingPx = anchoScreen - anchosStickers;
                //int paddingNormal = (int) getResources().getDimension(R.dimen.padding); //me entrega el padding en pixeles pero en float
                int paddingNew = (int) (paddingPx/2) + (padding/2);
                recyclerView.setPadding(paddingNew, padding/2, paddingNew, padding/2); //todo va en Px que son dependientes de la densidad
            }
            setNumColumns(numColums);//esto es en px
        }
    };
    private void setNumColumns(int numColumns) {
        if (this.numColumns != numColumns) {
            gridLayoutManager.setSpanCount(numColumns);
            this.numColumns = numColumns;
            if (adapterListaStickers != null) {
                adapterListaStickers.notifyDataSetChanged();
            }
        }
    }
    private float convertPixelsToDp(float px){
        return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    /*private final RecyclerView.OnScrollListener dividerScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            updateDivider(recyclerView);
        }

        @Override
        public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
            super.onScrolled(recyclerView, dx, dy);
            updateDivider(recyclerView);
        }

        private void updateDivider(RecyclerView recyclerView) {
            boolean showDivider = recyclerView.computeVerticalScrollOffset() > 0;
            if (divider != null) {
                divider.setVisibility(showDivider ? View.VISIBLE : View.INVISIBLE);
            }
        }
    };*/
    ////////////////////////////////ActivityResult of Add Sticker to Wapp//////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PACK) {
            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                final String validationError = data.getStringExtra("validation_error");
                if (validationError != null) {
                    if (BuildConfig.DEBUG) {
                        //validation error should be shown to developer only, not users.
                        BaseActivity.MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");
                    }
                    Log.e("ERROR VALIDATION", "Validation failed:" + validationError);
                }
            }else
                Toast.makeText(this,"Sticker Agregado a Wapp",Toast.LENGTH_LONG).show();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void initRecyclerView(int cantidad) {
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        // que formara el Recycler view en la actividad
        recyclerView.setLayoutManager(gridLayoutManager); //configuramos que el recycler view obtenga todas las caracteristicas de un LinearLayout
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(pageLayoutListener);
        //recyclerView.addOnScrollListener(dividerScrollListener);
    }

    @Override
    public AdapterListaStickers initAdapter(ArrayList<DataSticker> ListStickersStaticsBuy) {
        adapterListaStickers = new AdapterListaStickers(ListStickersStaticsBuy,this, this);
        return adapterListaStickers;
    }

    @Override
    public void startAdapter(AdapterListaStickers adapterListaStickers) {
        recyclerView.setAdapter(adapterListaStickers);
        container.hideShimmer();
        container.stopShimmer();
        container.setVisibility(View.GONE);
    }

    @Override
    public void updateYuanSale(int newSalario) {
        txtMonedas.setText(newSalario+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnlineNet()){
            ///////////////////EJECUTA CUANDO NO HUBO INTERNET DESDE QUE SE CREO LA ACTIVIDAD//////////////////////////////////////////
            if(!Conectividad){
                Toast.makeText(this, "TRY INIT ALL", Toast.LENGTH_SHORT).show();
                btnIntersticial.setVisibility(View.VISIBLE);
                btnRewarded.setVisibility(View.VISIBLE);
                progressBarIntersticialButton.setVisibility(View.GONE);
                progressBarRewardedButton.setVisibility(View.GONE);
                Conectividad = true;
                ///////////////////////////////////////////////////////////////////

                /////////////////////////////////////////////////////////////////////////////////////////////////
                if (!StatusAdwarded) {
                    //Toast.makeText(this, "TRY INIT ALL ADS", Toast.LENGTH_SHORT).show();
                    //inicializa nuevamente el objeto de anuncio Adwarded, al no haber internet.
                    admod.initREwardedAd(ID_ADMOD_REWARDED_STICKERS_1);
                    StatusAdwarded = true;
                }
            }
            ////////////////////////EJECUTA CUANDO SE FUE REPENTINAMENTE EL INTERNET DENTRO DE LA ACTIVIDAD SOLO PARA ADWARDED/////////////////////////////////////////////////////////////////////////
            if (!StatusAdwarded) {
                //Toast.makeText(this, "TRY INIT ALL ADS", Toast.LENGTH_SHORT).show();
                //inicializa nuevamente el objeto de anuncio Adwarded, al no haber internet.
                admod.initREwardedAd(ID_ADMOD_REWARDED_STICKERS_1);
                StatusAdwarded = true;
            }
            //Toast.makeText(this,"onRESUME",Toast.LENGTH_SHORT).show();
            //////////////////////////////SET BANNER///////////////////////////////////////////////////////////////
            admod.setBannerAdmod(R.id.adViewBannerSecond); //se actualiza el banner cada vez que resume la actividad

            show_Instruction_ButtonIntersticial();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //se actualiza el script cada vez que se pausa la app
        writeStickerPurchased(this, getListStickersScript());
    }

    @Override
    protected void onStop() {
        super.onStop();
        //se actualiza el script cada vez que se para la app
        writeStickerPurchased(this, getListStickersScript());
    }
/////////////////////////////////////////Metodos  adwarded////////////////////////////////////////////////////////
    @Override
    public void loadAd_Adwarded(Boolean isLoaded) {
        if (isLoaded){
            progressBarRewardedButton.setVisibility(View.GONE);
            btnRewarded.setVisibility(View.VISIBLE);
        }else
            StatusAdwarded = isLoaded;
    }

    @Override
    public void CloseAd_adwarded(Boolean wasClosed) {
        if (wasClosed){
            btnRewarded.setVisibility(View.GONE);
            progressBarRewardedButton.setVisibility(View.VISIBLE);
            //crea otro objeto, para pedir otro video de publicidad para el proximo clic
            admod.initREwardedAd(ID_ADMOD_REWARDED_STICKERS_1);
        }
    }

    @Override
    public void OpenAd_adwarded(Boolean isOpened) {
        if (isOpened){
            if (isOnlineNet())
                cuadroDialogoRecompensa =  new CuadroDialogoRecompensa(this, txtMonedas);
        }
    }

    @Override
    public void Rewarded_Permit(Boolean rewardedStatus, int ValRewarded) {
        if (rewardedStatus){
            giftYuanes(getBaseContext(), ValRewarded); //recibe el monto establecido en admod
            txtMonedas.setText(getYuanVal(View_StickersPack.this)+""); //actualiza el textview
            cuadroDialogoRecompensa.ShowDialogRewarded(ValRewarded+""); //imprime un dialogo mostrando si desea duplicar las monedas

        }
    }
/////////////////////////////////////////METODOS DE INTERSTICIAL ////////////////////////////////////
    @Override
    public void CloseAd_Intertisial(Boolean isCLosed) {
        if (isCLosed){
            NumbersTryIntersticial = 0;
            int rewarded  = getResources().getInteger(R.integer.Reward_Ad_Intersticial);
            giftYuanes(this, rewarded); //al abrir el anuncio, se le regala 30 monedas
            txtMonedas.setText(getYuanVal(View_StickersPack.this)+""); //actualiza el textview
            show_Instruction_ButtonsReward();
        }
    }

    @Override
    public void FailAd_Intersticial(int numbersTry) {
        if(numbersTry > 3){
            numbersTry++;
            NumbersTryIntersticial = numbersTry;
        }else{
            btnIntersticial.setVisibility(View.GONE);
            progressBarIntersticialButton.setVisibility(View.VISIBLE);
        }

        if (numbersTry == 1656){
            int rewarded  = getResources().getInteger(R.integer.Reward_No_Ad_Intersticial);
            giftYuanes(this,rewarded ); //recibe el monto establecido por no haber publicidad
            txtMonedas.setText(getYuanVal(View_StickersPack.this)+""); //actualiza el textview
        }
    }

    @Override
    public void preLoadAd_Intersticial(Boolean isPreloaded) {
    }

    //////////////////////////////// Set Listener item selection Menu COntext ////////////////////5


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 300:
                //adapterListaStickers.getItemId(item.getGroupId());
                Dialog_change_category_sticker(item.getGroupId(), "MOVER STICKER");
                accion = "MOVER";
                return true;
            case 301:
                Dialog_change_category_sticker(item.getGroupId(), "CLONAR STICKER");
                accion = "CLONAR";
                return true;
            case 302:
                Dialog_Code_for_Delete_Stickers(item.getGroupId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    ///////////////////////////////////////////Dialog for Entry to Add Stickers /////////////////////////////////
    private void Dialog_Code_for_Delete_Stickers(int groupId) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Eliminar Sticker");
        dialog.setMessage("Porfavor, ingresa el codigo de autorización para poder realizar esta acción.\n" +
                "Solo el personal de soporte dispone este codigo.");

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
                if (TextUtils.isEmpty(codigo)) {
                    codeBox.setError("Se requiere el codigo");
                } else {
                    ad.dismiss();
                    if (!readBooleanData(View_StickersPack.this, KEY_ENTRY_SUPPORT))
                        if (codigo.toString().equals("ANKAEDY-2020-NOVIEMBRE-28-4N4D1R-5T1K3R")) {
                            writeBooleanData(View_StickersPack.this, KEY_ENTRY_SUPPORT, true);
                            //code
                            adapterListaStickers.deleteSticker(groupId);

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
                        //code
                        adapterListaStickers.deleteSticker(groupId);

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

    EditText edCode;
   TextView txtTitle;
    Spinner spCategory;
    String Categoria = "", accion="";
    ///////////////////////////////DIALOG FOR CHANGE CATEGORY //////////////////////////////////////
    private void Dialog_change_category_sticker(int itemSelected, String Title) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(true); //para que no sea cancelable al tocar fuera de la pantalla
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_change_category_sticker);
        //txtId = (TextView) dialog.findViewById(R.id.txtIdChange);
        txtTitle = (TextView) dialog.findViewById(R.id.txtTitleMEnu);
        txtTitle.setText(Title);
        edCode = (EditText) dialog.findViewById(R.id.edtCode);
        spCategory = (Spinner) dialog.findViewById(R.id.spCategory);
        Button btnChange = (Button) dialog.findViewById(R.id.btnChange);

        ClicSpinnerCategorias();

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String tempItemSelected = parent.getSelectedItem().toString();
                if (!tempItemSelected.equals("<Categoria>")) {
                    Categoria = tempItemSelected;

                }else if (tempItemSelected.equals("<Categoria>")){
                    Categoria="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edCode.getText().toString();
                if (!Categoria.equals("")){
                    if (!readBooleanData(View_StickersPack.this, KEY_ENTRY_SUPPORT))
                        if (code.equals(""))
                            edCode.setError("Se requiere código");
                        else{
                            if (code.equals("ANKAEDY-2020-NOVIEMBRE-28-4N4D1R-5T1K3R")) {
                                writeBooleanData(View_StickersPack.this, KEY_ENTRY_SUPPORT, true);
                                //code
                                dialog.dismiss();
                                if(accion.equals("MOVER"))
                                   adapterListaStickers.changeCategorySticker(itemSelected, Categoria);
                                else if(accion.equals("CLONAR"))
                                    adapterListaStickers.cloneSticker(itemSelected, Categoria);

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
                        }

                    else {
                        //code
                        dialog.dismiss();
                        if(accion.equals("MOVER"))
                            adapterListaStickers.changeCategorySticker(itemSelected, Categoria);
                        else if(accion.equals("CLONAR"))
                            adapterListaStickers.cloneSticker(itemSelected, Categoria);
                    }
                }else
                    Toast.makeText(View_StickersPack.this, "Llene los campos",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void ClicSpinnerCategorias()
    {
        List<String> CategoriasList = null;
        List<DataPack> dataPacksList;
        dataPacksList = getDataPacksList(this);

        if(CategoriasList == null)
            CategoriasList= new ArrayList<>();
        else
            CategoriasList.clear();

        for(DataPack dataPack : dataPacksList)
            CategoriasList.add(dataPack.getPackName());

        CategoriasList.add(0,"<Categoria>");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_own, CategoriasList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
        // adapter.notifyDataSetChanged();
    }

    private void show_Instruction_ButtonIntersticial(){
        new MaterialIntroView.Builder(View_StickersPack.this)
                .enableIcon(true)
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(1000)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Intro: Presiona el boton para ganar " + getResources().getInteger(R.integer.Reward_Ad_Intersticial)+""
                        + " Yuans (Contiene Ads)")
                .setShape(ShapeType.RECTANGLE)
                .setTarget(findViewById(R.id.btnIntesticial))
                .setUsageId("intro_Intersticial") //THIS SHOULD BE UNIQUE ID
                .setMaskColor(getResources().getColor(R.color.blanco_alpha)) //pinta toda la pantalla para enfocar al boton
                // Permitir que esta superposición de presentación solo se muestre una vez. Evita que se muestren varias pantallas al mismo tiempo.
                // Útil si desea mostrar un paso de recorrido en un código que se llama varias veces
                .setIdempotent(true)
                .show();
    }

    private void show_Instruction_ButtonsReward(){
        new MaterialIntroView.Builder(View_StickersPack.this)
                .enableIcon(true) // Desactiva el icono de ayuda, el valor predeterminado es verdadero
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(2000)  //tiempo en el que se demora en aparecer
                .enableFadeAnimation(true) // Muestra el centro de animación de puntos del área de enfoque
                .performClick(true)  // Activa la operación de clic cuando el usuario hace clic en el área de enfoque.
                .setInfoText("Intro: Presiona el boton para ganar " + getResources().getInteger(R.integer.Reward_Ad_Adwarded)+""
                        + " Yuans (Contiene Ads)")
                .setShape(ShapeType.RECTANGLE)
                .setTarget(findViewById(R.id.flbtnMonedas))
                .setUsageId("intro_Rewarded") //THIS SHOULD BE UNIQUE ID
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