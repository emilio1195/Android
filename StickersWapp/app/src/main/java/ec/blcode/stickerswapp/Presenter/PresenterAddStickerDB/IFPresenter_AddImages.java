package ec.blcode.stickerswapp.Presenter.PresenterAddStickerDB;

public interface IFPresenter_AddImages {
    void CompletedStorageSticker(String UrlSticker, boolean Status, int position);
    void CompletedCloudSticker(boolean Status, int position);
}
