package ec.blcode.stickerswapp.Functions.BillingGoogle;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.Functions.Dialogos.CuadroDialogDonate;
import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.POJO.DataPurchase;
import ec.blcode.stickerswapp.R;

import static com.android.billingclient.api.BillingClient.BillingResponseCode.OK;
import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readYuanes;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_YUANES;
import static ec.blcode.stickerswapp.Functions.Constantes.SKU_DONATION_1;
import static ec.blcode.stickerswapp.Functions.Constantes.SKU_DONATION_20;
import static ec.blcode.stickerswapp.Functions.Constantes.SKU_DONATION_3;

public class BillingCLient_Google  implements PurchasesUpdatedListener, AcknowledgePurchaseResponseListener,
                                                BillingClientStateListener
                                                , ConsumeResponseListener, InterfazPurchase {
    private static BillingClient mBillingClient;
    private long mLastPurchaseClickTime = 0;
    private List<String> mSkuList = new ArrayList<>();
    private List<SkuDetails> mSkuDetailsList = new ArrayList<>();
    private CuadroDialogDonate cuadroDialogDonate;
    Context context;
    private String sku;
    //private List<Purchase> ListItemsPuchase;
    private SetProgressDialog setProgressDialog;
    private String comprasOtorgadas="";
    private String comprasPendientes="";
    private String estadoCompra = "";
    private String estadoPendiente="";
    int rewarded = 0;
    public BillingCLient_Google(Context context) {
        this.context = context;
        setProgressDialog = new SetProgressDialog(context);
    }

    //inicializa y Conecta el billing CLient
    public void initBillingClient(){
        /** IN-APP PURCHASES */
        // Initialize the list of all the in-app product IDs I use for this app
        mSkuList.add(SKU_DONATION_1);// donar 2 dollar
        mSkuList.add(SKU_DONATION_3);// donar 5 dollar
        mSkuList.add(SKU_DONATION_20);// donar 20 dollar
        //mPrefs = AppPrefs.getInstance(this); //inicialza un script sharedpreferences
        // Initialize the billing client
        setupBillingClient();

        //enlazamos las clases para que funcione la interfaz
        cuadroDialogDonate = new CuadroDialogDonate(this);

        // Apply the upgrades on my app according to the user's purchases
        //applyUpgrades();
    }
    public void verificarCompras(List<Purchase> ListItemsPuchase) {
        // Retrieve and loop all the purchases done by the user
        // Update all the boolean related to the purchases done in the shared preferences
        if (ListItemsPuchase != null)
            if (ListItemsPuchase.size() > 0) {
                for(Purchase purchase : ListItemsPuchase){
                    handlePurchase(purchase);//comprueba si la compra ya esta efectuada y luego aplica la compra en la app
                }
               if (estadoCompra.equals("PURCHASE")){
                   int val = readYuanes(context, KEY_YUANES);
                   writeYuanes(context, KEY_YUANES, val + rewarded);
                   rewarded=0;
                   //giftYuanes(context, rewarded);
                    setProgressDialog.getAlertDialog("GRACIAS POR DONAR",
                            "Te agradezco por apoyar al desarrollo de esta app.\n" +
                                    "Por tu donación(es) has recibido: \n" + comprasOtorgadas +
                                    "\n\nDisfrutalos. Gracias!!");
                   estadoCompra = "";
                   comprasOtorgadas ="";
                }
               if (estadoPendiente.equals("PENDING")) {
                   setProgressDialog.getAlertDialog("ESTADO DE COMPRAS",
                           "Tu compra se esta procesando. No cierres la app, solo minimizala si ya no la vas a usar.\n" +
                                   "Disculpa las molestias.\n" +
                                   "Tus compras en verificación son: " + comprasPendientes + "\n");
                   estadoPendiente = "";
                   comprasPendientes="";
               }
            }
        consumeCompras(ListItemsPuchase);
    }

    ////////////////////El método que configura el cliente de facturación está aquí,
    //                 junto con el método que utilizo para recuperar los productos disponibles
    //                 en la aplicación para la aplicación:
    private void setupBillingClient() {
        mBillingClient = BillingClient
                .newBuilder(context)
                .enablePendingPurchases() // Useful for physical stores
                .setListener(this)
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                List<Purchase> ListItemsPuchase = new ArrayList<>();
                if (billingResult.getResponseCode() == OK) {
                    Log.i("CONNECT STATUS","Success to connect billing");
                    // Load the available products related to the app from Google Play
                    getAvailableProducts();
                    //obtiene una lista de compras, es similar a la lista de SKU, pero no trae sus detalles;
                    ListItemsPuchase = mBillingClient.queryPurchases(INAPP).getPurchasesList();// Or SkuType.SUBS if subscriptions

                    verificarCompras(ListItemsPuchase); //aveces en el OnResume, la conctividad del billing aun no se completa por ello hay que aegurarse con el metodo en el listener
                                                    //cuando la app se vuelve a crear, cuando el ciclo se queda en onPause, on Resume debe lograr captar la compra completa

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                // TODO Note: It's strongly recommended that you implement your own connection retry policy and override the onBillingServiceDisconnected() method. Make sure you maintain the BillingClient connection when executing any methods.
                Log.i("CONNECT STATUS","finished the conectivity for service billing");
            }
        });
    }

    ///Recibe los productos dispobibles de pago de Google PLAY
    private void getAvailableProducts() {
        if (mBillingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(mSkuList)
                    .setType(INAPP)
                    .build();

            mBillingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                    if (billingResult.getResponseCode() == OK) {
                        mSkuDetailsList = skuDetailsList;
                    }
                }
            });
        }
    }


///////////////////////////para reconocer una compra en la app ////////////////////////////
    private void applyPurchase(Purchase purchase) {
        //SetProgressDialog setProgressDialog = new SetProgressDialog(context);
        switch (purchase.getSku()) {
            case SKU_DONATION_1:
                //mPrefs.setNoAdsPurchased(true);
                rewarded= rewarded + context.getResources().getInteger(R.integer.Reward_Donate1);
                break;
            case SKU_DONATION_3:
                //mPrefs.setCustomizationPurchased(true);
                rewarded = rewarded + context.getResources().getInteger(R.integer.Reward_Donate3);
                break;
            case SKU_DONATION_20:
                //mPrefs.setChartsPurchased(true);
                rewarded = rewarded + context.getResources().getInteger(R.integer.Reward_Donate20);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + purchase.getSku());
        }
        comprasOtorgadas = comprasOtorgadas + " -> " + rewarded + " Yuans \n";
        //se lo suscribe al grupo de donadores para notificaciones exclusivas
       FirebaseMessaging.getInstance().subscribeToTopic("Donors");
        // I remove the ads right away if purchases
        /*if(mPrefs.getNoAdsPurchased()) {
            destroyAds();
        }*/

    }

    /////////Cuando el usuario realiza una compra (permito compras en varios Fragmentos en mi aplicación),
    //       llamo a esta función en la Actividad principal (usando una interfaz):
    @Override
    public void purchase(String sku, Activity activity) {
        this.sku = sku;
        // Mis-clicking prevention, using threshold of 3 seconds
       /* if (SystemClock.elapsedRealtime() - mLastPurchaseClickTime < 3000){
            Log.d("Status", "Purchase click cancelled");
            return;
        }
        mLastPurchaseClickTime = SystemClock.elapsedRealtime();*/


        // Retrieve the SKU details
        if (mSkuDetailsList.size() > 0) {
            for (SkuDetails skuDetails : mSkuDetailsList) {
                // Find the right SKU
                if (sku.equals(skuDetails.getSku())) { //verifica si el item seleccionado a comprar esta en la lista de productos en google console
                     //String price = skuDetails.getPrice();
                    // Lanza el dialogo de compra de Google
                    BillingFlowParams flowParams = BillingFlowParams.newBuilder() //iniciar una solicitud de compra
                            .setSkuDetails(skuDetails) //se coloca el item seleccionado para la compra
                            .build();
                    mBillingClient.launchBillingFlow(activity, flowParams).getResponseCode();
                    break;
                }
            }
        }else
            Toast.makeText(context,"ERROR, PURCHASE LIST GOOGLE CONSOLE EMPTY",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

    }

    @Override
    // Evento salta cuando se pierde la conexión durante una compra
    public void onBillingServiceDisconnected() {
        mBillingClient.startConnection(this);
    }

    ///////////////////metodos heredados /////////////////////////////////////////
    @Override
    // entrega el resultado de la operación de compra mientras la app este ejecutandose y con disponibilidad de red
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> ListItemsPuchases) {
        if (billingResult.getResponseCode() == OK && ListItemsPuchases != null) {

            verificarCompras(ListItemsPuchases);
            /* for (Purchase purchase : ListItemsPuchase) {

                    //handlePurchase(purchase);
             }*/

            //consumeCompras();

        }  else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(context,"Donación Cancelada por el Usuario", Toast.LENGTH_LONG).show();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
            // Handle any other error codes.
            Toast.makeText(context, "Error de Conexión", Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(context,"Error, Solicitud Negada", Toast.LENGTH_LONG).show();
    }

    // Busca compras en el Servidor de Google y las marca como consumidas
    private void consumeCompras(List<Purchase> purchasesList) {
     /*   Purchase.PurchasesResult queryPurchases = mBillingClient.queryPurchases(INAPP);
        if (queryPurchases.getResponseCode() == OK) {
            List<Purchase> purchasesList = queryPurchases.getPurchasesList();*/
            if (purchasesList != null && !purchasesList.isEmpty()) {
                for (Purchase purchase : purchasesList) {
                    ConsumeParams params = ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
                    //método cumple el requisito de reconocimiento e indica que su aplicación ha otorgado derechos al usuario.
                    // Este método también permite que su aplicación haga que el producto único esté disponible para su compra nuevamente.
                    mBillingClient.consumeAsync(params, this);
                }
            }
        //}

    }

    @Override
    // Evento salta cuando se ha consumido un producto, Si responseCode = 0, ya se puede volver a comprar
    public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
        if (billingResult.getResponseCode() == OK) {
            Log.i("Pagos", "Token de Compra: " + purchaseToken + " consumida");
            setProgressDialog.getAlertDialog("COMPRAS PROCESADAS",
                    "Tus compras han sido procesadas, si alguna compra no te generó la entrega de tus Yuans, has clic " +
                            "en el menu y selecciona\n" +
                            "<<Reclamar Yuans (Donador)>>, para saber que hacer.\n" +
                            "Un abrazo y disculpa las molestias causadas.");
           /* int cont = readYuanes(context, KEY_CONTEO_PURCHASES_OK);
            cont++;
            writeYuanes(context, KEY_CONTEO_PURCHASES_OK, cont);
            int conteo = readYuanes(context, KEY_CONTEO_PURCHASES_OK);
            if (conteo >= 3)
                writeBooleanData(context, KEY_STATE_PENDING,false);*/
        } else {
            Log.i("Pagos", "Error al consumir compra, responseCode: " + billingResult.getResponseCode());
            //setProgressDialog.getAlertDialog("PROCESANDO COMPRA...",
              //      "Disculpa las molestias, aun no se confirma tu compra, cuando se confirme podras comprar nuevamente.");
        }
    }


    private void handlePurchase(Purchase purchase) {
        //purchase.getSku().equals(sku)
        CloudFirebase cloudFirebase;
        DataPurchase dataPurchase = new DataPurchase(purchase.getPurchaseToken(), purchase.getOrderId(), purchase.getSku());
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            Log.i("Purchase", "Compra realizada, disfrutala!");
            estadoCompra = "PURCHASE";
            applyPurchase(purchase);
            cloudFirebase = new CloudFirebase(context);
            cloudFirebase.addDataPurchase(dataPurchase);
            //cuando se borra del CLoud se da el valor de recompensa, mientras no se borre no se da
            //asi tener la garantia de que cuando se borra se grabaron los yuan al usuario
            cloudFirebase.deletePendingPurchase(purchase.getPurchaseToken());
            //////////////////////////////////////////////////////////////////
            // Grant entitlement to the user.


            sku = "";
            // Acknowledge the purchase if it hasn't already been acknowledged.
           /* if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, this);
            }*/
        }else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING){
             estadoPendiente = "PENDING";
            comprasPendientes = comprasPendientes +  "\n -> Order Id: " +  purchase.getOrderId();
            cloudFirebase = new CloudFirebase(context);
            cloudFirebase.addDataPendingPurchase(dataPurchase);
        }else {
            setProgressDialog.getAlertDialog("ERROR DE COMPRA", "Lo siento algo salió mal con tu método de pago, " +
                    "intentalo nuevamente o intenta con otro método de pago.\n" +
                    "Order Id: " + purchase.getOrderId() +"\n\n" +
                    "PROBLEMA #E2: Tarjeta Rechazada.");
        }
    }

    @Override
    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
        if (billingResult.getResponseCode() == OK) { //si la compra se reconoce
            //displayError(R.string.inapp_purchase_success, billingResult.getResponseCode());
        }
    }


    ///////////////Este método se utiliza para aplicar todas las actualizaciones / compras en la aplicación
    //              (con un ejemplo con la eliminación de los anuncios):
   /* private void applyUpgrades() {
        // No ads
        if (mPrefs.getNoAdsPurchased()) {
            destroyAds();
        } else {
            loadAds();
        }

        if (mPrefs.getCustomizationPurchased()) {
            // Allow customization
            // ...
        }

        if (mPrefs.getChartsPurchased()) {
            // Allow charts visualization
            // ...
        }
    }*/
}
