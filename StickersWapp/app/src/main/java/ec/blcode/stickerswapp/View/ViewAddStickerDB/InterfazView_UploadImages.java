package ec.blcode.stickerswapp.View.ViewAddStickerDB;

public interface InterfazView_UploadImages {
    public void dataTrayUploadedStorage(String urlDownload, Boolean StatusUpload);
    void CompletedTaskCloudTrayIcon(Boolean Status);
    void SuccessfulCloudTrayIcon(Boolean status);
    void SizeStickerImage (float SitckerSize);
}
