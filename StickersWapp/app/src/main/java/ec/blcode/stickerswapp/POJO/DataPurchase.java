package ec.blcode.stickerswapp.POJO;

public class DataPurchase {
    String TokenPurchase, OrderId, Sku;

    public DataPurchase() {
    }

    public DataPurchase(String tokenPurchase, String orderId, String sku) {
        TokenPurchase = tokenPurchase;
        OrderId = orderId;
        Sku = sku;
    }

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public String getTokenPurchase() {
        return TokenPurchase;
    }

    public void setTokenPurchase(String tokenPurchase) {
        TokenPurchase = tokenPurchase;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
