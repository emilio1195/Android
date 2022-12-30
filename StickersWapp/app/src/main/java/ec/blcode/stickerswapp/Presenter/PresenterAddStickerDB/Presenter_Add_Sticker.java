package ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import ec.blcode.stickerswapp.Functions.ImageUtils;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.View.ViewAddStickerDB.InterfazView_UploadImages;

public class Presenter_Add_Sticker {
    private static float sizeTray, sizeSticker;
    Activity activity;
    Context context;
    FirebaseFirestore database;
    InterfazView_UploadImages interfazView_uploadImages;
    IFPresenter_AddImages ifPresenter_addImages;
    private StorageReference FilePathStickerDB;

    public Presenter_Add_Sticker(FirebaseFirestore database, Context context) {
        this.database = database;
        this.context = context;
        //FilePathStickerDB = FirebaseStorage.getInstance().getReference();
    }

    public Presenter_Add_Sticker(Context context, InterfazView_UploadImages interfazView) {
        this.context = context;
        FilePathStickerDB = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance();
        this.interfazView_uploadImages = interfazView;
    }

    public Presenter_Add_Sticker(Context context, IFPresenter_AddImages interfazView) {
        this.context = context;
        FilePathStickerDB = FirebaseStorage.getInstance().getReference();
        database = FirebaseFirestore.getInstance();
        this.ifPresenter_addImages = interfazView;
    }

///////////////// UPLOAD TRY ICON CLOUD DATA ////////////////////////////////////////////////////////////////////
    public void uploadDataPackCloud(final DataPack dataPack) {
        database.collection("StaticsPacks").document(dataPack.getPackName())
                .set(dataPack)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        interfazView_uploadImages.CompletedTaskCloudTrayIcon(true);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        interfazView_uploadImages.SuccessfulCloudTrayIcon(true);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                interfazView_uploadImages.SuccessfulCloudTrayIcon(false);
                interfazView_uploadImages.CompletedTaskCloudTrayIcon(false);
            }
        });
    }
/////////////////////////////////////UPLOAD STICKER CLOUD DATA //////////////////////////////////////////////////////////////////////////
    public void UploadCloudSticker(final DataSticker dataSticker, final int position) {

        database.collection("StaticsPacks").document(dataSticker.getStickerCategoria())
                .collection("Stickers").document(dataSticker.getStickerId())
                .set(dataSticker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(context,"Sticker> " + dataSticker.getStickerNombre()  + " \n Uploaded."
                        //      ,Toast.LENGTH_LONG).show();
                       ifPresenter_addImages.CompletedCloudSticker(true, position);
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(context,"<Error> Fail Data Upload \n Sticker > " +dataSticker.getStickerNombre()
                //                  ,Toast.LENGTH_LONG).show();
                ifPresenter_addImages.CompletedCloudSticker(false, position);
                Log.w("TAG", "Error writing document", e);
            }
        });
    }
/////////////////////////////////////CONVERSION A OTRO FORMATO Y DIMNSION   ////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Uri convertToWebPSticker(Context context,Uri uriImage, int IndexId) {

        try {
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriImage);

            String dir = context.getFilesDir() + "/TempUris";
            File f = new File(dir);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            String path = dir + "/" + IndexId+"" + "_Sticker.webp";
            File file = new File(path);
            //file -> /data/user/0/ec.blcode.contentproviderejemplo/files/Groceria/Groceria/trayImage_Groceria.png
            if (file.exists()) file.delete();
            Log.w("Conversion Sticker: ", "path: " + path);

            FileOutputStream out = new FileOutputStream(file);
            byte[] bitmapdata = ImageUtils.compressImageToBytes(uriImage, 90, 512, 512, context, Bitmap.CompressFormat.WEBP);
            sizeSticker = bitmapdata.length / 1000;
            interfazView_uploadImages.SizeStickerImage(sizeSticker);
            //txtSizeSticker.setText("Size image = " +  size+"" + " Kb");
            out.write(bitmapdata);
            out.flush();
            out.close();
            Toast.makeText(context, "Conversion Completed", Toast.LENGTH_SHORT);
            return Uri.fromFile(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Uri convertToPNGTrayIcon(Context context, Uri uriImage) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriImage);

            String dir = context.getFilesDir() + "/TempUris";
            File f = new File(dir);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            String path = dir + "Temp_TryIcon.png";
            File file = new File(path);
            //file -> /data/user/0/ec.blcode.contentproviderejemplo/files/Groceria/Groceria/trayImage_Groceria.png
            if (file.exists()) file.delete();
            Log.w("Conversion TryIcon: ", "path: " + path);

            FileOutputStream out = new FileOutputStream(file);
            byte[] bitmapdata = ImageUtils.compressImageToBytes(uriImage, 90, 96, 96, context, Bitmap.CompressFormat.PNG);
            sizeTray = bitmapdata.length / 1000;
            //txtSizeIcon.setText("Size image = " + sizeTray +"" + " Kb");
            out.write(bitmapdata);
            out.flush();
            out.close();
            Toast.makeText(context, "Conversion Completed", Toast.LENGTH_SHORT);
            return Uri.fromFile(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uriImage;
    }

    public static float getSizeTray() {
        return sizeTray;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList getListCategory(String NameCollection) {
        final ArrayList<DataPack> dataPackList = new ArrayList<>();
        database.collection(NameCollection)
                //.whereEqualTo("capital",)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PAckDAta", document.getId() + " => " + document.getData());
                                //Recupera los datos en el mismo orden que los encuentra en la base de datos
                                DataPack dataPack = document.toObject(DataPack.class);
                                dataPack.setPackName(document.getId());
                                //se coloca el id, ya que en la conversion toObject no detecta el id del pack
                                //se coloca una version, para saber cuando se ha actualizado el pack con nuevos stickers y no hacer consultas nuevas cada rato
                                dataPackList.add(dataPack);
                            }
                            dataPackList.add(null);
                        } else {
                            Log.d("PackDAta", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return dataPackList;
    }

    ////////////////////////////////////////////UPLOAD STORAGE //////////////////////////////////////////////////////////////////////////

    /////////////////////////////////Storage Try Icon //////////////////////////////////////////////
    public void UpImageStorage(Uri uriConversion, String FileStorage, String nameCategory, final ProgressBar pbUploadingStorage) {
        boolean ConversionOk = false;
        if (uriConversion != null ) {
            Toast.makeText(context, "***** CONVERSION OK ******", Toast.LENGTH_SHORT).show();
            ConversionOk = true;
        } else {
            Toast.makeText(context, "<<<< ERROR CONVERSION >>>>", Toast.LENGTH_LONG).show();
        }

        if (ConversionOk) {

            StorageReference uploadStickerDB = FilePathStickerDB.child("stickers").child(FileStorage).child(nameCategory + ".png");

            uploadStickerDB.putFile(uriConversion)
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int progreso = (int) ((100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                            pbUploadingStorage.setProgress(progreso);
                            //setProgressDialog.IncProgressDialog("Uploading Storage...",
                            //      "Uploading imagen Icono to Storage", progreso);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getTask().getResult().getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();
                            String UrlTaryIcon = downloadUri.toString();
                            interfazView_uploadImages.dataTrayUploadedStorage(UrlTaryIcon, true);
                            //versionNew = 1;
                            //txtVersionNew.setText(versionNew + "");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    interfazView_uploadImages.dataTrayUploadedStorage("Error Uploaded, Try Again", false);
                }
            });
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////Storage Sticker //////////////////////////////////////////////
    public void UpStickerStorage(Uri uriConversion, String FileStorage, final ProgressBar pbUploadingStorage, String id, final int position) {
        StorageReference uploadStickerDB = FilePathStickerDB.child("stickers").child(FileStorage).child(id + ".webp");

        uploadStickerDB.putFile(uriConversion)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        int progreso = (int) ((70 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                        pbUploadingStorage.setProgress(progreso);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getTask().getResult().getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();
                        String UrlSticker = downloadUri.toString();
                        ifPresenter_addImages.CompletedStorageSticker(UrlSticker, true, position);
                        //versionNew = 1;
                        //txtVersionNew.setText(versionNew + "");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ifPresenter_addImages.CompletedStorageSticker("Error Uploaded, Try Again", false, position);
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////



   /* public void simulateProgressDialog(String Titulo, String Mensaje){
        pbUploadingStorage = new ProgressDialog(context);
        pbUploadingStorage.setTitle(Titulo);
        pbUploadingStorage.setMessage(Mensaje);
        pbUploadingStorage.setIcon(R.mipmap.ic_launcher);
        pbUploadingStorage.setCancelable(false);
        pbUploadingStorage.setMax(100);
        pbUploadingStorage.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pbUploadingStorage.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pbUploadingStorage.dismiss();
            }
        });
        pbUploadingStorage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (pbUploadingStorage.getProgress() < pbUploadingStorage.getMax()) {
                        Thread.sleep(120);
                        pbUploadingStorage.incrementProgressBy(10);
                    }
                    pbUploadingStorage.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }*/
}
