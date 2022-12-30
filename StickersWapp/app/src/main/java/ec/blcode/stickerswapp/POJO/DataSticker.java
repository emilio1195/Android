package ec.blcode.stickerswapp.POJO;

public class DataSticker {
    //los parametros deben ser privados para que funcione al enviar a la data base, porque si son publicos da errores
    //los getters y setters deben ser publicos.
    private  String StickerDescripcion;
    private  String StickerCredito;
    private  String StickerCategoria;
    private  String StickerPrecio;
    private int StickerDescargas;
    private  String StickerUrl;
    private  String StickerTryIconURL;
    private String StickerId;


    public DataSticker(){ }  // se crea un constructor vacio ya que se van a recuperar datos de fireStore y se requiere tener la clase sin parametros en su contructor
    //para que no se problemas, ya que al dar la referencia de esta clase es para decirle que debe extraer datos similares a los que se encuentran en sus getters y setters


    public DataSticker(String stickerId, String stickerDescripcion, String stickerCredito, String stickerCategoria, String stickerPrecio,
                       int stickerDescargas, String stickerUrl, String stickerTryIconURL) {
        StickerDescripcion = stickerDescripcion;
        StickerCredito = stickerCredito;
        StickerCategoria = stickerCategoria;
        StickerPrecio = stickerPrecio;
        StickerDescargas = stickerDescargas;
        StickerUrl = stickerUrl;
        StickerTryIconURL = stickerTryIconURL;
        StickerId = stickerId;
    }

    public String getStickerId() {
        return StickerId;
    }

    public void setStickerId(String stickerId) {
        StickerId = stickerId;
    }

    public String getStickerDescripcion() {
        return StickerDescripcion;
    }

    public void setStickerDescripcion(String stickerDescripcion) {
        StickerDescripcion = stickerDescripcion;
    }

    public String getStickerCredito() {
        return StickerCredito;
    }

    public void setStickerCredito(String stickerCredito) {
        StickerCredito = stickerCredito;
    }

    public String getStickerCategoria() {
        return StickerCategoria;
    }

    public void setStickerCategoria(String stickerCategoria) {
        StickerCategoria = stickerCategoria;
    }

    public String getStickerPrecio() {
        return StickerPrecio;
    }

    public void setStickerPrecio(String stickerPrecio) {
        StickerPrecio = stickerPrecio;
    }

    public int getStickerDescargas() {
        return StickerDescargas;
    }

    public void setStickerDescargas(int stickerDescargas) {
        StickerDescargas = stickerDescargas;
    }

    public String getStickerUrl() {
        return StickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        StickerUrl = stickerUrl;
    }

    public String getStickerTryIconURL() {
        return StickerTryIconURL;
    }

    public void setStickerTryIconURL(String stickerTryIconURL) {
        StickerTryIconURL = stickerTryIconURL;
    }
}


