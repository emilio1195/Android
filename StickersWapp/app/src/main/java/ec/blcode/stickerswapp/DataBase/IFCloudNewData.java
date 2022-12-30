package ec.blcode.stickerswapp.DataBase;

import java.util.ArrayList;

import ec.blcode.stickerswapp.POJO.DataPack;

public interface IFCloudNewData {
    void DataPackUpdated(ArrayList<DataPack> dataPacksList);
    void DeleteSticker(Boolean isdeleted, int position);
    void ChangeStickerCategory(Boolean isChanged);
    void CloneStickerCategory(Boolean isCloned);
}
