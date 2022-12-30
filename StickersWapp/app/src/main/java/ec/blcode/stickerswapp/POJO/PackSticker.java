package ec.blcode.stickerswapp.POJO;

import java.util.ArrayList;

public class PackSticker {
    private DataPack dataPack;
    private ArrayList <DataSticker> dataSticker;

    public PackSticker(DataPack dataPack, ArrayList<DataSticker> dataSticker) {
        this.dataPack = dataPack;
        this.dataSticker = dataSticker;
    }

    public DataPack getDataPack() {
        return dataPack;
    }

    public void setDataPack(DataPack dataPack) {
        this.dataPack = dataPack;
    }

    public ArrayList<DataSticker> getDataSticker() {
        return dataSticker;
    }

    public void setDataSticker(ArrayList<DataSticker> dataSticker) {
        this.dataSticker = dataSticker;
    }
}
