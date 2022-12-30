package ec.blcode.stickerswapp.Functions.Admod;

public interface InterfazAdmod {
    void loadAd_Adwarded(Boolean isLoaded);
    void CloseAd_adwarded(Boolean wasClosed);
    void OpenAd_adwarded(Boolean isOpened);
    void Rewarded_Permit(Boolean rewardedStatus, int ValRewarded);
    void CloseAd_Intertisial(Boolean isCLosed);
    void FailAd_Intersticial(int NumbersTry);
    void preLoadAd_Intersticial(Boolean isPreloaded);
}
