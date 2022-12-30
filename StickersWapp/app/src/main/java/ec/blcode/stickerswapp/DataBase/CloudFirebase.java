package ec.blcode.stickerswapp.DataBase;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.giftYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_UUID;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.POJO.DataDonor;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataPurchase;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.POJO.DataUser;

public class CloudFirebase {
    FirebaseFirestore db;
    private ArrayList dataStickerList, dataPackList;
    final String TAG = "Get Data Firebase: ";
    InterfazDataFireStore interfazDataFireStore;
    IFCloudNewData ifCloudNewData;
    Context context;

    public CloudFirebase(InterfazDataFireStore iDataFireStore, Context context) {
        interfazDataFireStore = iDataFireStore;
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public CloudFirebase(Context context) {
        interfazDataFireStore = null;
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public CloudFirebase(Context context, IFCloudNewData ifCloudNewData) {
        interfazDataFireStore = null;
        db = FirebaseFirestore.getInstance();
        this.context = context;
        this.ifCloudNewData = ifCloudNewData;
    }
    public void readStickersCollectionBD(final String NameColletion, final String Category, final Activity activity) {
        dataStickerList = new ArrayList<>();
        db.collection(NameColletion).document(Category).collection("Stickers")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    //Toast.makeText(activity, "Leyendo...", Toast.LENGTH_SHORT).show();
                    List<DocumentSnapshot> listData = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot data : listData) {
                        DataSticker dataSticker = data.toObject(DataSticker.class);
                        if(dataSticker != null)
                             dataStickerList.add(dataSticker);
                        else
                            dataStickerList.add(new DataSticker());
                    }


                    Collections.sort(dataStickerList, new ComparadorIdStickers());
                    interfazDataFireStore.ListDataStickers(dataStickerList); //enviamos el ArrayList mediante una Interfaz, ya que es la manera correcta de enviarlos
                }
            }
        });

    }

    class ComparadorIdStickers implements Comparator<DataSticker> {
        @Override
        public int compare(DataSticker o1, DataSticker o2) {
           // return o1.getStickerId().compareTo(o2.getStickerId());
            return extractInt(o2) - extractInt(o1);
        }

        int extractInt(DataSticker dataSticker) {
            //String num = s.replaceAll("\\D", "");  //elimina todas las letras y espacios y obtiene solo el numero
                                                    // de una cadena > hola 5 > 5
            int fin = dataSticker.getStickerId().indexOf("_");
            String index = dataSticker.getStickerId().substring(0,fin);
            return index.isEmpty() ? 0 : Integer.parseInt(index);
        }
    }


    public void readIdsPacksDB(String NameCollection) {
        dataPackList = new ArrayList<>();
        db.collection(NameCollection)
                //.whereEqualTo("capital",)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //Recupera los datos en el mismo orden que los encuentra en la base de datos
                                DataPack dataPack = document.toObject(DataPack.class);
                                dataPack.setPackName(document.getId());
                                //se coloca el id, ya que en la conversion toObject no detecta el id del pack
                                //se coloca una version, para saber cuando se ha actualizado el pack con nuevos stickers y no hacer consultas nuevas cada rato
                                if(dataPackList != null)
                                     dataPackList.add(dataPack);
                                else
                                    dataPackList.add(new DataPack());
                            }
                            interfazDataFireStore.ListDataPack(dataPackList);
                            //Una vez completada la lectura de las categorias, se procede hacer la lectura de sus stickers
                            //se pasa la lista de categorias, y se inicia en la pos cero de la lista
                            //readStickersPreviewBD(dataPackList,0, new ArrayList<PackSticker>());


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void readIdsPacksDBForUpdate(String NameCollection) {
        dataPackList = new ArrayList<>();
        db.collection(NameCollection)
                //.whereEqualTo("capital",)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //Recupera los datos en el mismo orden que los encuentra en la base de datos
                                DataPack dataPack = document.toObject(DataPack.class);
                                dataPack.setPackName(document.getId());
                                //se coloca el id, ya que en la conversion toObject no detecta el id del pack
                                //se coloca una version, para saber cuando se ha actualizado el pack con nuevos stickers y no hacer consultas nuevas cada rato
                                dataPackList.add(dataPack);
                            }
                            //interface_view_listPackFragment.startAdapter(interface_view_listPackFragment.initAdapter(dataPackArrayList));
                            ifCloudNewData.DataPackUpdated(dataPackList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /////////////////////////////////////////change Sticker  a categoria ///////////////////////////////////
    public void changeCategoriaSticker(DataSticker dataSticker, String Categoria, int position) {
        String categoriaAnterior = dataSticker.getStickerCategoria();
        dataSticker.setStickerCategoria(Categoria);
        db.collection("StaticsPacks").document(Categoria)
                .collection("Stickers").document(dataSticker.getStickerId())
                .set(dataSticker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Movido a " + Categoria,Toast.LENGTH_SHORT).show();
                        ifCloudNewData.ChangeStickerCategory(true);
                        dataSticker.setStickerCategoria(categoriaAnterior);
                        deleteSticker(dataSticker, position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Error al Cambiar",Toast.LENGTH_SHORT).show();
                ifCloudNewData.ChangeStickerCategory(false);
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////DElete Sticker///////////////////////////////////////////////////////////////////////
    public  void deleteSticker(DataSticker dataSticker, int position){
        db.collection("StaticsPacks").document(dataSticker.getStickerCategoria())
                .collection("Stickers")
            .document(dataSticker.getStickerId()).delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    Toast.makeText(context,"Eliminado",Toast.LENGTH_SHORT).show();
                    ifCloudNewData.DeleteSticker(true, position);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting document", e);
                    ifCloudNewData.DeleteSticker(false, position);
                    //Toast.makeText(context,"Error Delete",Toast.LENGTH_SHORT).show();
                }
            });
}
    /////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////clone Sticker  a categoria ///////////////////////////////////
    public void cloneSticker(DataSticker dataSticker, String Categoria) {
        String categoriaAnterior = dataSticker.getStickerCategoria();
        dataSticker.setStickerCategoria(Categoria);
        db.collection("StaticsPacks").document(Categoria)
                .collection("Stickers").document(dataSticker.getStickerId())
                .set(dataSticker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Clonado a " + Categoria,Toast.LENGTH_SHORT).show();
                        ifCloudNewData.CloneStickerCategory(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Error al CLONAR",Toast.LENGTH_SHORT).show();
                ifCloudNewData.CloneStickerCategory(false);
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////


    public void addUserDB(DataUser dataUser){
        // Add a new document with a generated ID
        db.collection("Users").document(dataUser.getUuid())
                .set(dataUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Succesful Register",Toast.LENGTH_SHORT).show();
                        interfazDataFireStore.SuccesfullUpUser(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(context,"Fail Register",Toast.LENGTH_SHORT).show();
                    interfazDataFireStore.SuccesfullUpUser(true);
            }
        });
    }

    public  void addDataPurchase(DataPurchase dataPurchase){
        db.collection("Users").document(readDataAppText(context, KEY_UUID).trim())
                .collection("Purchases").add(dataPurchase);
              /*  .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context,"Compra Registrada a la DataBase",Toast.LENGTH_SHORT).show();
                    }
                });*/
    }

    public  void addDataPendingPurchase(DataPurchase dataPurchase){
        db.collection("Users").document(readDataAppText(context, KEY_UUID).trim())
                .collection("Pending_Purchases")
                .document(dataPurchase.getTokenPurchase()).set(dataPurchase);
               /* .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Compra Pendiente Registrada a la DataBase",Toast.LENGTH_SHORT).show();
                    }
                });*/
    }

    public  void deletePendingPurchase(String TokenPurchase){

        db.collection("Users").document(readDataAppText(context, KEY_UUID).trim())
                .collection("Pending_Purchases")
                .document(TokenPurchase).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                        //Toast.makeText(context,"Compra Pendiente Eliminada de la DataBase",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        //Toast.makeText(context,"Error Delete",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public  void addDataIp(String Ip){
        Map<String, Object> dataIp = new HashMap<>();
        dataIp.put("IP", Ip);
        dataIp.put("latestUpdateTimestamp", FieldValue.serverTimestamp());

        db.collection("Users").document(readDataAppText(context, KEY_UUID).trim())
                .collection("IP").document(Ip).set(dataIp);
    }

    /////////////////////////////////////UPDATE DESCARGS STICKER  CLOUD DATA //////////////////////////////////////////////////////////////////////////
    public void UpdateNumbersDownloadStickers(final DataSticker dataSticker) {

        db.collection("StaticsPacks").document(dataSticker.getStickerCategoria())
                .collection("Stickers").document(dataSticker.getStickerId())
                .update("StickerDescargas", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Successful Purchase",Toast.LENGTH_SHORT).show();
                        //Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                });
    }

    /////////////////////////////////RECIBIR YUANS POR DONAR ///////////////////////////////////////////////////////////////

    public void getDataDonor(final String Uid, final String Name) {
        SetProgressDialog setProgressDialog = new SetProgressDialog(context);;

        db.collection("Donors").document(Uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DataDonor dataDonor = documentSnapshot.toObject(DataDonor.class);
                if (dataDonor != null){
                    if (!dataDonor.getCobrado()) {
                        int yuans = Integer.parseInt( dataDonor.getYuans());
                        giftYuanes(context,yuans);
                        setBooleanCobrado(Uid, true);
                        setProgressDialog.getAlertDialog("PROCESO COMPLETADO",
                                "Hola " + Name + ", has Recibido tus " + dataDonor.getYuans() + " Yuans\n\n" +
                                        "Disfruta tus Yuans. Un abrazo!");
                    } else{
                        setProgressDialog.getAlertDialog("PROCESO FALLIDO",
                                "Ya cobraste Tus Yuans el día: " +
                                        documentSnapshot.getDate("Cobrado_Fecha"));

                    }
                }else{
                    setProgressDialog.getAlertDialog("PROCESO FALLIDO",
                            "No has abierto una Solicitud para recibir tus Yuanes por Donar.\n" +
                                    "Contáctanos con tu problema si es que han pasado varios días y no recibiste tus Yuans, " +
                                    "para revisar nuestro sistema sobre tu caso.");
                }

            }
        });

    }
    ////////////////////////////////////Actualizar Boolean si el usuario cobra sus Yuans////////////////////////////////////
    public  void setBooleanCobrado(String Uid, boolean Statuscobrado){
        Map<String, Object> dataCobrado = new HashMap<>();
        dataCobrado.put("cobrado", true);
        dataCobrado.put("Cobrado_Fecha", FieldValue.serverTimestamp());

        db.collection("Donors").document(Uid)
                .update(dataCobrado)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Yuans Recibidos",Toast.LENGTH_SHORT).show();
                    }
                });
    }

//DocumentSnapshot{key=StaticsPacks/Categoria_Expresiones, metadata=SnapshotMetadata{hasPendingWrites=false, isFromCache=false}, doc=Document{key=StaticsPacks/Categoria_Expresiones, data=com.google.firebase.firestore.model.ObjectValue@1a1bf6a, version=SnapshotVersion(seconds=1603785256, nanos=974076000), documentState=SYNCED}}

 /*   public void readStickersPreviewBD(final ArrayList<DataPack> ListdataPack, final int pos, final ArrayList<PackSticker> ListPackSticker) {
        dataStickerList = new ArrayList<>();
        if (pos < ListdataPack.size() ) {
            final DataPack dataPack = ListdataPack.get(pos);
            //Result.append("\n" + dataPack.getPackName() +" -> ");
            //StaticsPacks/Categoria_Cariños/Stickers
            db.collection("StaticsPacks").document(dataPack.getPackName()).collection("Stickers")
                    .orderBy("stickerNombre")//le decimos que ordene los datos por nombre
                    .limit(10) //limitamos que solo traiga 10 elementos
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                //Toast.makeText(activity,"Leyendo...",Toast.LENGTH_SHORT).show();
                                List<DocumentSnapshot> listData = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot data : listData) {
                                    DataSticker dataSticker = data.toObject(DataSticker.class);
                                    dataStickerList.add(dataSticker);
                                    //Result.append("\n" + "** " + dataSticker.getStickerNombre() +" -> ");
                                }
                                //Result.append("\n" + ">>>>>>" + "Size List > " + dataStickerList.size()+"");
                                //interfazDataFireStore.ListStickersPreview(dataStickerList); //enviamos el ArrayList mediante una Interfaz, ya que es la manera correcta de enviarlos
                                ListPackSticker.add(new PackSticker(dataPack, dataStickerList));
                                int posi = pos;
                                posi++;
                                ///se realiza recursividad con el objetivo de que cada vez q se complete la consulta de una categoria
                                //puede realizar otra consulta de la siguiente categoria de la lista
                                readStickersPreviewBD(ListdataPack, posi, ListPackSticker);
                                if (posi == ListdataPack.size() - 1 )
                                    interfazDataFireStore.ListPacksStickersPreview(ListPackSticker);
                            }
                        }
                    });
        }
    }*/

    public void disableRedDB(FirebaseFirestore db){
        db.disableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete (@NonNull Task < Void > task) {
                        // Do offline things
                        Toast.makeText(context,"Recuperando Data Cache", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void enableRedDB(FirebaseFirestore db) {
        db.enableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Do online things
                        Toast.makeText(context,"Recuperando Data Cloud", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}



