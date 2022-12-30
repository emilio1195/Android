package ec.blcode.stickerswapp.Functions.Admod;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;
import ec.blcode.stickerswapp.R;

import static com.google.android.gms.ads.AdRequest.ERROR_CODE_INTERNAL_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NETWORK_ERROR;
import static com.google.android.gms.ads.AdRequest.ERROR_CODE_NO_FILL;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readYuanes;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_REWARDED_AD;

public class Admod {

    private int ERROR_SERVER_FOR_NETWORK = 20;
    private  RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    InterfazAdmod interfazAdmod;
    Activity activity;
    Context context;
    private int ErroCodeIntersticial = 2515;

    public Admod(InterfazAdmod interfazAdmod, Activity activity) {
        this.interfazAdmod = interfazAdmod;
        this.activity = activity;
        this.context = activity;
    }
    public Admod(Activity activity) {
        this.interfazAdmod = null;
        this.activity = activity;
        this.context = activity;
    }

    public  void setBannerAdmod(int id_AdBanner){

        AdView mAdView =(AdView) activity.findViewById(id_AdBanner);
        AdRequest adRequest = new AdRequest.Builder().build();  ///solicita el permiso para el anuncio
        mAdView.loadAd(adRequest);
    }
    public  void setBannerAdmodFragment(View view, int id_AdBanner){

        AdView mAdView =(AdView) view.findViewById(id_AdBanner);
        AdRequest adRequest = new AdRequest.Builder().build();  ///solicita el permiso para el anuncio
        mAdView.loadAd(adRequest);
    }

    public  void initBannerAdmod(){
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public void initREwardedAd(String unitId_adRewarded){
        rewardedAd = new RewardedAd(context, unitId_adRewarded);
        //final SetProgressDialog setProgressDialog = new SetProgressDialog(context);

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                int rewarded = rewardedAd.getRewardItem().getAmount();
                if (readYuanes(context, KEY_REWARDED_AD) != rewarded)
                    writeYuanes(context, KEY_REWARDED_AD, rewarded);
                 //Toast.makeText(context,"Cargando anuncio de recompensa",Toast.LENGTH_SHORT).show();
                interfazAdmod.loadAd_Adwarded(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                 //Toast.makeText(context,"Fail load Ad \n" + adError,Toast.LENGTH_LONG).show();
                //setProgressDialog.getAlertDialog("Error", adError.getMessage() +"\n" + adError.getCode());
                int errorCode = adError.getCode();
                if(errorCode == ERROR_CODE_INTERNAL_ERROR)
                    ERROR_SERVER_FOR_NETWORK = errorCode;
                interfazAdmod.loadAd_Adwarded(false);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    public void setRewardedAd(){

        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                  //  if (ERROR_SERVER_FOR_NETWORK == 20)
                        interfazAdmod.OpenAd_adwarded(true);
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    //dialog.dismiss();
                        interfazAdmod.CloseAd_adwarded(true);
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    //giftYuanes(activity.getBaseContext(),reward.getAmount());
                    //txtMonedas.setText(getYuanVal(activity.getBaseContext())+"");
                    interfazAdmod.Rewarded_Permit(true, reward.getAmount());
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.

                    interfazAdmod.OpenAd_adwarded(false);
                    interfazAdmod.CloseAd_adwarded(false);
                    interfazAdmod.Rewarded_Permit(false, 0);
                }
            };
            rewardedAd.show(activity, adCallback);
        } else {
            Log.d("AD_REWARDED: ", "The rewarded ad wasn't loaded yet.");
            interfazAdmod.loadAd_Adwarded(false);
            interfazAdmod.OpenAd_adwarded(false);
            interfazAdmod.CloseAd_adwarded(false);
            interfazAdmod.Rewarded_Permit(false, 0);
        }
    }

    public void initIntertisialAd(String AdUnitId){
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(AdUnitId);
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public  void LoadIntersticial_clic(final int NumbersTry){
        final SetProgressDialog setProgressDialog = new SetProgressDialog(context);
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    interfazAdmod.preLoadAd_Intersticial(true);
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    ErroCodeIntersticial = loadAdError.getCode();
                    if( ErroCodeIntersticial != ERROR_CODE_INTERNAL_ERROR) {
                        if (ErroCodeIntersticial == ERROR_CODE_NO_FILL) { //si no hay anuncios, se le regala 10 monedas
                            interfazAdmod.FailAd_Intersticial(1656);
                            int yuans  = context.getResources().getInteger(R.integer.Reward_No_Ad_Intersticial);
                            setProgressDialog.getAlertDialog("Recompensa",
                                    "No hay publicidad por el momento, pero te regalamos " + yuans+"" + " Yuans");
                        } else if (ErroCodeIntersticial == ERROR_CODE_NETWORK_ERROR) {
                            setProgressDialog.getAlertDialog("Error",
                                    "Hubo un problema lo sentimos. \n Revisa tu conexión a Internet");
                        } else {
                            interfazAdmod.FailAd_Intersticial(NumbersTry);
                            setProgressDialog.getAlertDialog("Error",
                                    "Hubo un problema, Intentalo Nuevamente (" + NumbersTry + "/3)" + "\n" + loadAdError.getCode());
                            if (NumbersTry > 3) {
                                setProgressDialog.getAlertDialog("Error",
                                        "Hubo un problema lo sentimos. \n Intentalo mas tarde");
                            } else {
                                // Load the next interstitial.
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        }
                    }

                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the interstitial ad is closed.
                    // Load the next interstitial.
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    int yuans  = context.getResources().getInteger(R.integer.Reward_Ad_Intersticial);
                    setProgressDialog.getAlertDialog("Recompensa",
                            "Has recibido " + yuans+"" +" Yuans, por ver la publicidad.");
                   // if (ErroCodeIntersticial == 2515)
                    interfazAdmod.CloseAd_Intertisial(true);
                }
            });

        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
           // interfazAdmod.preLoadAd_Intersticial(false);
            interfazAdmod.CloseAd_Intertisial(false);
        }
    }
}


