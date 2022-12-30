package ec.blcode.stickerswapp.POJO;

public class DataPack {
    //los parametros deben ser privados para que funcione al enviar a la data base, porque si son publicos da errores
    //los getters y setters deben ser publicos.

    private String PackName, PackTrayIcon, PackLinkPlayStore;
    private  int PackVersion;

    public DataPack(){}

    public DataPack(String packName, int packVersion, String packTrayIcon, String packLinkPlayStore) {
        this.PackName = packName;
        this.PackVersion = packVersion;
        this.PackTrayIcon = packTrayIcon;
        this.PackLinkPlayStore = packLinkPlayStore;
    }

    public String getPackLinkPlayStore() {
        return PackLinkPlayStore;
    }

    public void setPackLinkPlayStore(String packLinkPlayStore) {
        PackLinkPlayStore = packLinkPlayStore;
    }

    public String getPackName() {
        return PackName;
    }

    public void setPackName(String packName) {
        PackName = packName;
    }

    public int getPackVersion() {
        return PackVersion;
    }

    public void setPackVersion(int packVersion) {
        PackVersion = packVersion;
    }

    public String getPackTrayIcon() {
        return PackTrayIcon;
    }

    public void setPackTrayIcon(String packTrayIcon) {
        PackTrayIcon = packTrayIcon;
    }
}
