package ec.blcode.stickerswapp.View.ViewAddStickerDB;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readYuanes;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeDataPacksJSON;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_ID_IMG;
import static ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB.Presenter_Add_Sticker.convertToPNGTrayIcon;
import static ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB.Presenter_Add_Sticker.getSizeTray;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import ec.blcode.stickerswapp.Adapter.AdapterListImagesLoad;
import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.DataBase.IFCloudNewData;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB.Presenter_Add_Sticker;
import ec.blcode.stickerswapp.R;

public class View_Upload_Stickers extends AppCompatActivity implements InterfazView_UploadImages, IFCloudNewData {

    private static final int CODE_IMAGES_SELECTED = 1466;
    private ArrayList<Uri> listImages;
    private RecyclerView recyclerView;
    private String nameCategory;
    ProgressBar pbUploadingStorage, pbUploadingCloud;
    Presenter_Add_Sticker presenter_add_sticker;
    private Button btnUpload;
    private Button btnClose;
    private ProgressBar pbUploadingStorage2;
    private ArrayList<Float> listSizesImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__upload__stickers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.rvListAdds);
        final FloatingActionsMenu fbMenu = findViewById(R.id.fbMenu);
        FloatingActionButton fbAddStickers = findViewById(R.id.fbAddStickers);
        FloatingActionButton fbAddCategory = findViewById(R.id.fbAddCategory);
        FloatingActionButton fbEdit = findViewById(R.id.fbEditUriIndex);
        FloatingActionButton fbDeleteTempUris = findViewById(R.id.fbDeleteTempUri);
        FloatingActionButton fbClearScreen = findViewById(R.id.fbClearScreen);
        presenter_add_sticker = new Presenter_Add_Sticker(this, this);

        fbEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbMenu.collapse();
                Dialog_ChangeIndex();
            }
        });

        fbDeleteTempUris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fbMenu.collapse();
                AlertDialog.Builder builder = new AlertDialog.Builder(View_Upload_Stickers.this);
                builder.setTitle("Borrar Temporales");
                builder.setMessage("Seguro Quiere Borrar los archivos temporales?\n" +
                        "Se recomienda Eliminar, siempre y cuando finalice de cargar todos las imagenes a la base de datos.");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = getBaseContext().getFilesDir() + "/TempUris";
                        File dir = new File(path);
                        //comprueba si es directorio.
                        if (dir.isDirectory()) {
                            //obtiene un listado de los archivos contenidos en el directorio.
                            String[] hijos = dir.list();
                            //Elimina los archivos contenidos.
                            for (int i = 0; i < hijos.length; i++) {
                                new File(dir, hijos[i]).delete();
                            }
                            Snackbar.make(view, "Se ha eliminado los archivos Temp.", BaseTransientBottomBar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.anil)).setTextColor(getResources().getColor(R.color.acent_TextColor))
                                    .show();

                        } else
                            Snackbar.make(view, "No se ha eliminado el directorio Temp.", BaseTransientBottomBar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.Cancel)).setTextColor(getResources().getColor(R.color.acent_TextColor))
                                    .show();
                    }
                });
                builder.setNegativeButton("Cancelar",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        fbAddStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbMenu.collapse();

                if (listImages == null)
                    listImages = new ArrayList<>();
                else
                    listImages.clear();

                if (listSizesImages == null)
                     listSizesImages = new ArrayList<>();
                else
                    listSizesImages.clear();

                openFile();
            }
        });

        fbAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbMenu.collapse();
                DialogAddCategory();
            }
        });

        fbClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbMenu.collapse();

                if (listImages != null)
                    listImages.clear();

                if (listSizesImages != null)
                    listSizesImages.clear();

                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        initRecyclerView();
    }


    private void openFile() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        i.setType("image/*");
        startActivityForResult(i, CODE_IMAGES_SELECTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ///////////////////////////RESULT FOR IMAGE STIKER ///////////////////////////////////////////
        if(requestCode == CODE_IMAGES_SELECTED  && resultCode == RESULT_OK ){
            int idImgTotal = readYuanes(this, KEY_ID_IMG); //se lee el ultimo valor id guardado
            int startID = idImgTotal;
            int cont=0;
            if(data!=null){
                if(data.getClipData()!=null) {
                    ClipData clipData = data.getClipData(); //Where data is param intent of onActivityForResult
                    idImgTotal = idImgTotal + clipData.getItemCount(); //el valor que queda es el index para el proximo sticker
                    for (int i = startID; i < idImgTotal; i++) {

                        Uri uri = clipData.getItemAt(cont).getUri();
                        cont++;
                        //listImages.add(uri);
                        listImages.add(presenter_add_sticker.convertToWebPSticker(this, uri, i));
                    }
                }else{
                    Uri uri = data.getData();
                    listImages.add(presenter_add_sticker.convertToWebPSticker(this, uri, startID));
                   // listSizesImages.add(getSizeSticker());
                    startID++;
                    idImgTotal = startID; //se aumenta una posicion para el futuro sticker
                }
                 writeYuanes(this, KEY_ID_IMG, idImgTotal); //Se escribe el ultimo valor de ID
                startAdapter(initAdapter(listImages, listSizesImages));
                //recyclerView.getAdapter().notifyItemChanged(0, listImages);
            }
        }///////////////////////////////////RESULT FOR SELECT AND UPLOAD TRAY ICON //////////////////////////////////////////////
        else if(requestCode == CODE_TRY_SELECTED  && resultCode == RESULT_OK && data != null){
            Uri uriImagePick = data.getData();
            Uri uriTrayIcon = convertToPNGTrayIcon(this, uriImagePick);
            imgTrayIcon.setImageURI(uriTrayIcon);
            txtSize.setText(  "Size image = " + getSizeTray()+"" + " Kb");
            pbUploadingStorage2.setVisibility(View.VISIBLE);
            presenter_add_sticker.UpImageStorage(uriTrayIcon, "TrayImage",nameCategory, pbUploadingStorage);
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final int CODE_TRY_SELECTED = 2978;
    ImageView imgTrayIcon, imgStorage, imgCloud;
    EditText edtCategoria;
    TextView txtSize, txtUrl;

    public void DialogAddCategory() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //Solicitamos que e cuadro de dialogo no tenga un Titulo
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //coloca el fondo del dialogo de manera transparente
        dialog.setContentView(R.layout.cardview_add_category);
        imgTrayIcon = (ImageView) dialog.findViewById(R.id.imgTrayCategory);
        imgStorage = (ImageView) dialog.findViewById(R.id.imgStorageTray);
        imgCloud = (ImageView) dialog.findViewById(R.id.imgCloudTray);
        pbUploadingStorage = (ProgressBar) dialog.findViewById(R.id.pbUploadingStorage);
        pbUploadingStorage2 = (ProgressBar) dialog.findViewById(R.id.pbLoadingStorage2);
        pbUploadingCloud = (ProgressBar) dialog.findViewById(R.id.pbLoadingCloud);
        edtCategoria = (EditText) dialog.findViewById(R.id.edtCategoriaNew);
        txtSize = (TextView) dialog.findViewById(R.id.txtsizeTrayIcon);
        txtUrl = (TextView) dialog.findViewById(R.id.txtUrlCategoryIcon);
        btnUpload = (Button) dialog.findViewById(R.id.btnUploadCLoud);
        btnClose = (Button) dialog.findViewById(R.id.btnClose);
        Button btnCloseAux = (Button) dialog.findViewById(R.id.btnCloseAux);
        edtCategoria.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        btnCloseAux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbUploadingCloud.setVisibility(View.VISIBLE);
                presenter_add_sticker.uploadDataPackCloud(new DataPack(nameCategory, 1, txtUrl.getText().toString(),""));
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgTrayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = edtCategoria.getText().toString();
                if(!category.equals("")){
                    String txtCategoria = category;
                    txtCategoria = txtCategoria.trim().replace(" ", "_");
                    nameCategory = txtCategoria;
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    startActivityForResult(i, CODE_TRY_SELECTED);
                }else
                    edtCategoria.setError("Obligatorio Nombre Categoria");
            }
        });

        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        // que formara el Recycler view en la actividad
        recyclerView.setLayoutManager(linearLayoutManager); //configuramos que el recycler view obtenga todas las caracteristicas de un LinearLayout

    }

    public AdapterListImagesLoad initAdapter(ArrayList<Uri> ListImagesSelected, ArrayList<Float> listSizesImages) {
        AdapterListImagesLoad adapterListImagesLoad = new AdapterListImagesLoad(ListImagesSelected, listSizesImages, this);
        return adapterListImagesLoad;
    }

    public void startAdapter(AdapterListImagesLoad adapterListImagesLoad) {
        recyclerView.setAdapter(adapterListImagesLoad);
        adapterListImagesLoad.notifyDataSetChanged();
    }

    @Override
    public void dataTrayUploadedStorage(String urlDownload, Boolean StatusUpload) {
        txtUrl.setText(urlDownload);
        if(StatusUpload) {
            imgStorage.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_outline_30));
            btnUpload.setEnabled(true);
            pbUploadingStorage2.setVisibility(View.GONE);
        }
    }

    @Override
    public void CompletedTaskCloudTrayIcon(Boolean Status) {
        if (!Status) {
            pbUploadingCloud.setVisibility(View.GONE);
            Toast.makeText(this, "ERROR UPLOAD CLOUD", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void SuccessfulCloudTrayIcon(Boolean status) {
        if (status) {
            imgCloud.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_outline_30));
            pbUploadingCloud.setVisibility((View.GONE));
            btnClose.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.GONE);
            pbUploadingCloud.setVisibility(View.GONE);
            CloudFirebase cloudFirebase = new CloudFirebase(this, this);
            cloudFirebase.readIdsPacksDBForUpdate("StaticsPacks");

        } else {
            Toast.makeText(this, "ERROR UPLOAD CLOUD", Toast.LENGTH_LONG).show();
            pbUploadingCloud.setVisibility(View.GONE);
        }
    }

    @Override
    public void SizeStickerImage(float SitckerSize) {
            listSizesImages.add(SitckerSize);
    }

    @Override
    public void DataPackUpdated(ArrayList<DataPack> dataPacksList) {
        writeDataPacksJSON(this,dataPacksList);
    }

    @Override
    public void DeleteSticker(Boolean isdeleted, int position) {

    }

    @Override
    public void ChangeStickerCategory(Boolean isChanged) {

    }

    @Override
    public void CloneStickerCategory(Boolean isCloned) {

    }


    ///////////////////////////////////////////Dialog for Change Index Uri /////////////////////////////////
    private void Dialog_ChangeIndex() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cambiar Index Uri");
        dialog.setMessage("Ingresa el Index desde el cual quieres continuar enumerando a los stickers.");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText indexBox = new EditText(this);
        indexBox.setLines(1);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(50, 0, 50, 10);
        indexBox.setLayoutParams(buttonLayoutParams);
        indexBox.setHint("<<<<<< Example: 32 >>>>>>");
        indexBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(indexBox);

        final EditText creatorBox = new EditText(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            creatorBox.setAutofillHints("index");
        }

        dialog.setView(layout);

        dialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(View_Upload_Stickers.this, "El nuevo Index - Uri es: " + readYuanes(View_Upload_Stickers.this, KEY_ID_IMG)+""
                                    , Toast.LENGTH_LONG).show(); //se lee el ultimo valor id guardado )
            }
        });

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
                Editable index = indexBox.getText();
                if (TextUtils.isEmpty(index)) {
                    indexBox.setError("Se requiere un index");
                } else {
                    ad.dismiss();
                    writeYuanes(View_Upload_Stickers.this, KEY_ID_IMG, Integer.parseInt(index.toString())); //Se escribe el ultimo valor de ID
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

            AlertDialog.Builder builder = new AlertDialog.Builder(View_Upload_Stickers.this);
            builder.setTitle("Volver Ventana Principal");
            builder.setMessage("Seguro quieres regresar a la ventana principal?");
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

}

