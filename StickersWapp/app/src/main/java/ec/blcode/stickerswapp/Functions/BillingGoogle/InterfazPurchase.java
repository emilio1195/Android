package ec.blcode.stickerswapp.Functions.BillingGoogle;

import android.app.Activity;

import com.android.billingclient.api.BillingResult;

public interface InterfazPurchase {
    // Evento salta cuando se ha consumido un producto, Si responseCode = 0, ya se puede volver a comprar
    public void purchase(String sku, Activity activity);
}
