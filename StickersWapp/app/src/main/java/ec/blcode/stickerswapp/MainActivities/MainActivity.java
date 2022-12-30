package ec.blcode.stickerswapp.MainActivities;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeBooleanData;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.giftYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_INTRO_STATUS;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_LOGIN_STATUS;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_UUID;
import static ec.blcode.stickerswapp.Functions.Network_Conectivity_Status.isOnlineNet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ec.blcode.stickerswapp.DataBase.CloudFirebase;
import ec.blcode.stickerswapp.DataBase.InterfazDataFireStore;
import ec.blcode.stickerswapp.Functions.Dialogos.CuadroDialog_AdditionalUserInfo;
import ec.blcode.stickerswapp.Functions.Dialogos.DialogPoliticaPrivacidad;
import ec.blcode.stickerswapp.Functions.Dialogos.Interfaz_Additional_Info;
import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.Functions.NewUserIntroActivity;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.POJO.DataUser;
import ec.blcode.stickerswapp.POJO.PackSticker;
import ec.blcode.stickerswapp.R;

public class MainActivity extends AppCompatActivity implements Interfaz_Additional_Info, InterfazDataFireStore {
    private static final int CODE_SIGN_IN = 6597;
    //codigo de publicidad
    //ca-app-pub-1607581231141881~2341519939
    Button btnGoogle, btnFB;
    //Button btnIngresar;
    GoogleSignInClient mGoogleSignInClient;
    ProgressBar pbIniciando;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginManager loginManager;
    DataUser dataUser;
    private Intent goToCategoryScreen;
    private ProgressDialog progressDialog;
    private SetProgressDialog setProgressDialog;
    private boolean statusPolitica = false;

    //private String tokenFCM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGoogle = (Button) findViewById(R.id.btnAutGoogle);
        btnFB = (Button) findViewById(R.id.btnAutFB);
        pbIniciando = (ProgressBar) findViewById(R.id.pbIniciando);
        // Initialize Firebase Auth

        mAuth = FirebaseAuth.getInstance();
        initAuthGoogle();
        initAuthFacebook();
        if (!toShowIntro()) {
            startActivityForResult(new Intent(this, NewUserIntroActivity.class), 1114);
        }

       /* Button btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbIniciando.setVisibility(View.VISIBLE);
                Intent screenStickers = new Intent(MainActivity.this, MainScreenStickers.class);
                startActivity(screenStickers);
                MainActivity.this.finish();
            }
        });*/

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbIniciando.setVisibility(View.VISIBLE);
                /*Intent screenStickers = new Intent(MainActivity.this, MainScreenStickers.class);
                startActivity(screenStickers);
                MainActivity.this.finish();*/
                pbIniciando.setVisibility(View.VISIBLE);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, CODE_SIGN_IN);
            }
        });

        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbIniciando.setVisibility(View.VISIBLE);
                faceBookLogin();
            }
        });

        //Glide.with(MainActivity.this).load(R.drawable.tenor).into(img2);
    }

    private void getTokenCustomForNotifications() {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String tokenFCM = task.getResult();
                        dataUser.setTokenFCM(tokenFCM);
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TOKEN_PUSH_NOTIFICATION", tokenFCM);
                        //Toast.makeText(MainActivity.this, tokenFCM, Toast.LENGTH_SHORT).show();
                    }
                });
        firebaseMessaging.subscribeToTopic("Log-in");
    }

    private boolean toShowIntro() {
        return readBooleanData(this, KEY_INTRO_STATUS);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!statusPolitica) {
            new DialogPoliticaPrivacidad(MainActivity.this);
            statusPolitica = true;
        }
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
    }

    private void initAuthGoogle() {
        // Configurar el inicio de sesión para solicitar el ID del usuario, la dirección de correo electrónico y
        // perfil. El ID y el perfil básico están incluidos en DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Cree un GoogleSignInClient con las opciones especificadas por gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initAuthFacebook() {
        // Initialize Facebook Login button
        //AppEventsLogger.activateApp(getApplication());
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void faceBookLogin() {
        /*final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Logging....");
        dialog.show();*/
        loginManager = LoginManager.getInstance();
        //es el encargado de pedir los permisos para dichos datos y muestra la ventana para el login
        loginManager.logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("Facebook Login> ", "facebook:onSuccess:" + loginResult);
                if (AccessToken.getCurrentAccessToken() != null) {
                    //GraphRequest entrega mas informacion sobre el usuario
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }
            }

            @Override
            public void onCancel() {
                Log.d("Facebook Login> ", "facebook:onCancel");
                pbIniciando.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook Login> ", "facebook:onError", error);
                View view = findViewById(R.id.contenedorMain);
                Snackbar.make(view, "No se pudo Iniciar Sesion, Intentalo Nuevamente", Snackbar.LENGTH_SHORT).show();
                pbIniciando.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Token> ", "handleFacebookAccessToken:" + token);
        mAuth = FirebaseAuth.getInstance();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Facebook Credential> ", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                dataUser = new DataUser(user.getUid(), user.getDisplayName(), "", "", user.getEmail(),
                                        user.getPhoneNumber(), user.getPhotoUrl().toString(), "tokenFCM");

                                //  Log.i("Facebook Data User> ", user.getDisplayName() + ", " + user.getPhoneNumber()
                                //                               + ", " + user.getEmail() + ", \n" +
                                //                                                          user.getPhotoUrl().toString());

                                saveDataUser(dataUser);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook Credential> ", "signInWithCredential:failure", task.getException());
                            View view = findViewById(R.id.contenedorMain);
                            Snackbar.make(view, "No se pudo Iniciar Sesion, Intentalo Nuevamente", Snackbar.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (!readDataAppText(this, KEY_UUID).equals("")) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            //updateUI(currentUser);
            if (currentUser != null) {
                Intent goToCategoryScreen = new Intent(getApplicationContext(), MainScreenStickers.class);
                startActivity(goToCategoryScreen);
                this.finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Google Login STATUS> ", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google Login STATUS> ", "Google sign in failed", e);
                pbIniciando.setVisibility(View.INVISIBLE);
                View view = findViewById(R.id.contenedorMain);
                Snackbar.make(view, "No se pudo Iniciar Sesion, Intentalo Nuevamente", Snackbar.LENGTH_SHORT).show();
                // ...
            }
        } else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == 2319) {
            Uri uri = data.getData();
            getContentResolver().takePersistableUriPermission(Objects.requireNonNull(uri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else if (requestCode == 1114) {
            //makeIntroNotRunAgain();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google Credential", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dataUser = new DataUser(user.getUid(), user.getDisplayName(), "", "", user.getEmail(),
                                    user.getPhoneNumber(), user.getPhotoUrl().toString(), "tokenFCM");
                            //Log.d("Google Data User> ",user.getUid()+ user.getDisplayName() + ", " + user.getPhoneNumber()
                            //                                        + ", "  + user.getEmail() + ", \n" +
                            //                                                                  user.getPhotoUrl().toString());
                            saveDataUser(dataUser);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google Credential", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                            pbIniciando.setVisibility(View.INVISIBLE);
                            View view = findViewById(R.id.contenedorMain);
                            Snackbar.make(view, "No se pudo Iniciar Sesion, Intentalo Nuevamente", Snackbar.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void saveDataUser(DataUser dataUser) {
        getTokenCustomForNotifications();
        new CuadroDialog_AdditionalUserInfo(MainActivity.this,
                MainActivity.this, dataUser);
    }

    @Override
    public void aditionalInfoUser(DataUser dataUser) {
        setProgressDialog = new SetProgressDialog(this);
        progressDialog = setProgressDialog.getProgressDialog("REGISTRANDO...", "Espera Porfavor, estamos registrandote en el sistema");
        progressDialog.show();
        /////////////////////////se crea Intent y se empaqueta el objeto para enviarlo mediante bundle /////////////////////
        goToCategoryScreen = new Intent(this, MainScreenStickers.class);
        Bundle inforUser = new Bundle();
        inforUser.putSerializable("InforUser", dataUser);
        goToCategoryScreen.putExtras(inforUser);
        ////////////////////////////////Se envia la infor a la base de datos ////////////////////////////////////
        if (isOnlineNet()) {
            CloudFirebase cloudFirebase = new CloudFirebase(this, this);
            cloudFirebase.addUserDB(dataUser);
        }

    }

    @Override
    public void ListDataStickers(ArrayList<DataSticker> listDataStickers) {

    }

    @Override
    public void ListDataPack(ArrayList<DataPack> dataPackArrayList) {

    }

    @Override
    public void ListStickersPreview(ArrayList<DataSticker> dataListPreview) {

    }

    @Override
    public void ListPacksStickersPreview(ArrayList<PackSticker> listPackSticker) {

    }

    @Override
    public void SuccesfullUpUser(Boolean Uploaded) {
        if (Uploaded) {
            progressDialog.dismiss();
            if (!readBooleanData(this, KEY_LOGIN_STATUS)) {
                giftYuanes(getBaseContext(), getResources().getInteger(R.integer.Reward_Login)); //recibe un adwarded for login
                //writeDataApp(this, KEY_LOGIN_STATUS, true);
            }
            FirebaseMessaging.getInstance().subscribeToTopic("Log-in");
            makeIntroNotRunAgain();
            startActivity(goToCategoryScreen);
            MainActivity.this.finish();
        } else {
            progressDialog.dismiss();
            setProgressDialog.getAlertDialog("Error...", "Ocurrió un problema al registrarte. " +
                    "\n Revisa tu conexión a internet e intentalo nuevamente");
        }

    }

    private void makeIntroNotRunAgain() {
        writeBooleanData(this, KEY_INTRO_STATUS, true);
    }
}


////////////////////////////////////////////GET DINAMIC LINK /////////////////////////////////////////////////
   /* public  void RecuperarInformacionInvitacion(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            //createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    }
                });
    }*/

    /*private void createAnonymousAccountWithReferrerInfo(final String referrerUid) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Keep track of the referrer in the RTDB. Database calls
                        // will depend on the structure of your app's RTDB.
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("referred_by").setValue(referrerUid);
                    }
                });
    }*/
