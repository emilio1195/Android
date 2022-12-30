package ec.blcode.stickerswapp.Functions.Dialogos;

import static ec.blcode.stickerswapp.CodeAddSticker2Wapp.StickerBook.ExistPackStickerId;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.getYuanVal;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.payYuan;
import static ec.blcode.stickerswapp.Functions.Books.StickersPurchasedBook.AddNewStickerToScript;
import static ec.blcode.stickerswapp.Functions.Books.StickersPurchasedBook.WithListedCategory;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_LINK_ANDROID_APP;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ec.blcode.stickerswapp.BuildConfig;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.DataArchiver;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.StickerBook;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.WhatsAppBasedCode.StickerPack;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.InterfazSaveStickerFill;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.InterfazSaveStickerImage;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.InterfazSaveTrayImage;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.SaveFillSticker;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.SaveSticker;
import ec.blcode.stickerswapp.CodeAddSticker2Wapp.model.SaveTrayImage;
import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.Functions.Archivers.InterfazOpYuan;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.R;

public class CuadroDialogoBuy implements InterfazSaveTrayImage, InterfazSaveStickerImage, InterfazSaveStickerFill {
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";

    public static final int ADD_PACK = 200;

    Context context;
    DataSticker dataSticker;
    Button btnComprar, btnCancelar, btnAddWp;
    ImageView imgSticker;
    TextView txtDescrip, txtPrecioSticker, txtIdSticker;
    SetProgressDialog setProgressDialog;
    ProgressBar progressBarAddWapp;
    final Dialog dialog;
    InterfazOpYuan ViewInterfaz;
    SaveTrayImage saveTrayImage = new SaveTrayImage(this);
    SaveSticker saveSticker = new SaveSticker(this);
    private Uri uriTrayFile;
    private Uri uriStickerFill;
    private StickerPack stickerPack;
    private boolean ExistStickerPack;

    public CuadroDialogoBuy (final Context context, final DataSticker dataSticker, InterfazOpYuan ViewInterfaz){
        this.context = context;
        this.dataSticker = dataSticker;
        this.ViewInterfaz = ViewInterfaz;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(false); //para que no sea cancelable al tocar fuera de la pantalla
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_sticker_buy);
        btnComprar = (Button) dialog.findViewById(R.id.btnComprar);
        btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        btnAddWp = (Button) dialog.findViewById(R.id.btnAddWapp);
        progressBarAddWapp = (ProgressBar) dialog.findViewById(R.id.progressBarAdd);
        imgSticker = (ImageView) dialog.findViewById(R.id.imgSticker);
        txtDescrip = (TextView) dialog.findViewById(R.id.txtdescripcion);
        txtPrecioSticker = (TextView) dialog.findViewById(R.id.txtPrecio);
        txtIdSticker = (TextView) dialog.findViewById(R.id.txtIdSticker);
        setProgressDialog = new SetProgressDialog(context);
        putData();  //coloca los datos correspondientes del sticker en el dialogo
        FoundStickerInScript();
        /////////////////////Obtenemos un booleano si existe o no el Pack de la categoria correspondiente
        if (!ExistPackStickerId(dataSticker.getStickerCategoria()))
            ExistStickerPack = false;
        else
            ExistStickerPack = true;
        /////////////////////////////////////////////////////////////////////////////////////////////////
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paySticker();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAddWp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddWp.setVisibility(View.GONE);
                progressBarAddWapp.setVisibility(View.VISIBLE);
                SaveDataSticker2Script();
                //dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void putData(){
        txtDescrip.setText("Descripción: " + dataSticker.getStickerDescripcion() + "\n(Credito: " + dataSticker.getStickerCredito() + " )");
        txtPrecioSticker.setText(dataSticker.getStickerPrecio());
        int fin = dataSticker.getStickerId().indexOf(".");
        txtIdSticker.setText("Id >>> " + dataSticker.getStickerId().substring(0,fin+7));
        Glide.with(context).load(dataSticker.getStickerUrl()).placeholder(R.drawable.animview)
                .fitCenter().into(imgSticker);
    }

    private void paySticker(){
        int valSticker = Integer.parseInt(txtPrecioSticker.getText().toString());
        Boolean paid = payYuan(valSticker,context); //si el valor del precio es menor al salario, se procede el pago y retorna true
        if (paid){
            btnComprar.setVisibility(View.GONE);
            btnAddWp.setVisibility(View.VISIBLE);
            ViewInterfaz.updateYuanSale(getYuanVal(context)); // mediante interfaz se actualiza el TextView que muestra el saldo
            AddNewStickerToScript(context, dataSticker);
            CloudFirebase cloudFirebase = new CloudFirebase(context);
            cloudFirebase.UpdateNumbersDownloadStickers(dataSticker);
        }else{
            dialog.dismiss();
            setProgressDialog.getAlertDialog("FAIL PURCHASE","La cantidad de monedas es insuficiente, para realizar la compra. " +
                    "\n Gana monedas >> viendo anuncios o donando a la app.");
        }
    }

    private  void FoundStickerInScript(){
        boolean existSticker = WithListedCategory(dataSticker.getStickerId(), dataSticker.getStickerUrl(), context);
        if (existSticker){
            btnComprar.setVisibility(View.GONE);
            btnAddWp.setVisibility(View.VISIBLE);
        }
        else{
            btnComprar.setVisibility(View.VISIBLE);
             btnAddWp.setVisibility(View.GONE);
        }
    }

    private void SaveDataSticker2Script(){
        saveTrayImage.SaveImageTrayFile(context, dataSticker); //guarda el TryIcon en el direcctori
        //verifica las 2 uris y escribe los datos en el Script
    }

    private void addStickerPackToWhatsApp(StickerPack sp) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        Log.w("IS IT A NEW IDENTIFIER?", sp.getIdentifier());
        intent.putExtra(EXTRA_STICKER_PACK_ID, sp.getIdentifier());
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, sp.getName());
        //getBaseContext().getContentResolver().update("content://com.myapp.provide/rMyTable/167", values, null, null);
        try {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, ADD_PACK);
        } catch (ActivityNotFoundException e) {
            //Toast.makeText(context, R.string.error_adding_sticker_pack, Toast.LENGTH_LONG).show();
            progressBarAddWapp.setVisibility(View.GONE);
            btnAddWp.setVisibility(View.VISIBLE);
        }
    }

    private void createNewStickerPackAndOpenIt(String namePack, String creator, String CategoriaID, Uri trayImage){
        StickerPack sp = new StickerPack(
                CategoriaID,
                namePack,
                creator,
                trayImage,
                "",
                "",
                "",
                "",
                "1.00",
                context);
        sp.setAndroidPlayStoreLink(readDataAppText(context, KEY_LINK_ANDROID_APP));
        ///////////////////////////////////Se colocan los Sticker de relleno para cada Pack///////////////////////////////////////////////////////
        SaveFillSticker saveFillSticker = new SaveFillSticker(this);
        saveFillSticker.SaveImageFill(context, CategoriaID, R.drawable.presentacion,"Presentacion");
        sp.addFillSticker("Presentacion", uriStickerFill);
        saveFillSticker.SaveImageFill(context, CategoriaID, R.drawable.agradecimiento,"Agradecimiento");
        sp.addFillSticker("Agradecimiento", uriStickerFill);
        ///////////////////////////////////////////////////////////////////////////////////////////
        StickerBook.addStickerPackExisting(sp);
    }



    private void UpdatVersionPack(){
        stickerPack = StickerBook.getStickerPackById(dataSticker.getStickerCategoria());
        //se obtiene la version actual del paquete del .xml
        float version = (float) (StickerBook.getVersionStickerPack(dataSticker.getStickerCategoria()) + 0.1);
        //edita y actualiza la version del paquete existente.
        stickerPack.setImageDataVersion(String.valueOf(version));
    }

    @Override
    public void UriSaveStickerFill(Uri uri) {
        this.uriStickerFill = uri;
    }

    @Override
    public void UriSaveStickerImage(Uri uriStickerFile) {
        //se trae el packSticker creado para anadir Stickers
        if(!ExistStickerPack) {
            stickerPack = StickerBook.getStickerPackById(dataSticker.getStickerCategoria());
            stickerPack.addSticker(dataSticker, uriStickerFile);
        }else
             stickerPack.addSticker(dataSticker, uriStickerFile);

        if (uriTrayFile != null && uriStickerFile != null) {
            Boolean statusWrite = DataArchiver.writeStickerBookJSON(StickerBook.getAllStickerPacks(), context);
            if (statusWrite){
                //StickerPack sendstickerPack = StickerBook.getStickerPackById(dataSticker.getStickerCategoria());
                addStickerPackToWhatsApp(stickerPack);
                dialog.dismiss();
            }
        }
    }

    @Override
    public void UriSaveTrayImage(Uri uri, DataSticker dataSticker) {
        this.uriTrayFile = uri;

        if (!ExistStickerPack)
            createNewStickerPackAndOpenIt(dataSticker.getStickerCategoria(), "Stickers Wapp EC (PlayStore)",
                    dataSticker.getStickerCategoria(), uriTrayFile);
        else{
            UpdatVersionPack();
            // DataArchiver.EditUpdateVersionStickerPack(this, String.valueOf(version));
        }

        saveSticker.SaveStickerFile(context, dataSticker);  //guarda el Sticker en el direcctorio
    }


}
