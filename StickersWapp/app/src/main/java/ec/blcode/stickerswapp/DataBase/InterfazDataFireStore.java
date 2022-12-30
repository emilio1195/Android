package ec.blcode.stickerswapp.DataBase;

import java.util.ArrayList;

import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.POJO.PackSticker;

public interface InterfazDataFireStore {
    public void ListDataStickers(ArrayList<DataSticker> listDataStickers);
    public void ListDataPack(ArrayList<DataPack> dataPackArrayList);
    public void ListStickersPreview(ArrayList<DataSticker> dataListPreview);
    void ListPacksStickersPreview(ArrayList<PackSticker> listPackSticker);
    public void SuccesfullUpUser(Boolean Uploaded);
}
