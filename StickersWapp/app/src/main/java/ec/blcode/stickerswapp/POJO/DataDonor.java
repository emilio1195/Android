package ec.blcode.stickerswapp.POJO;

public class DataDonor {
   // private String Uid;
    private String Yuans;
    private Boolean Cobrado;


    public DataDonor() {
    }

    public DataDonor(String yuans, Boolean cobrado) {
        Yuans = yuans;
        Cobrado = cobrado;
    }

    public String getYuans() {
        return Yuans;
    }

    public void setYuans(String yuans) {
        Yuans = yuans;
    }

    public Boolean getCobrado() {
        return Cobrado;
    }

    public void setCobrado(Boolean cobrado) {
        Cobrado = cobrado;
    }
}
