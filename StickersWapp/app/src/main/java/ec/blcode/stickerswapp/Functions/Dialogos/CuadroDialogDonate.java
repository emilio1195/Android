package ec.blcode.stickerswapp.Functions.Dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ec.blcode.stickerswapp.Functions.BillingGoogle.InterfazPurchase;

import static ec.blcode.stickerswapp.Functions.Constantes.SKU_DONATION_1;
import static ec.blcode.stickerswapp.Functions.Constantes.SKU_DONATION_20;
import static ec.blcode.stickerswapp.Functions.Constantes.SKU_DONATION_3;

public class CuadroDialogDonate {
    private static InterfazPurchase interfazPurchase;
    public CuadroDialogDonate(InterfazPurchase interfazPurchase){
        this.interfazPurchase = interfazPurchase;
    }
    public static void setDialogDonate(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("GRACIAS POR TU DONACION");
        builder.setMessage("Apreciamos tu apoyo, para poder mantener en funcionamiento esta aplicación y poder subir más contenido. " +
                "           \n\n ¿Cuánto le gustaría donar?");

       /* builder.setPositiveButton("Un Shot de Tequila - 5$",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        //startInAppPurchase("5_dollar_donation");
                        interfazPurchase.purchase("5_dollar_donation", (Activity) context);
                    }
                });
*/
        builder.setNeutralButton("Recibir 1,200 Yuans - 1$",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                       // startInAppPurchase("1_dollar_donation");
                        interfazPurchase.purchase(SKU_DONATION_1, (Activity) context);
                    }
                });

        builder.setNegativeButton("Recibir 4,000 Yuans - 3$",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        //startInAppPurchase("3_dollar_donation");
                        interfazPurchase.purchase(SKU_DONATION_3, (Activity) context);
                    }
                });

        builder.setPositiveButton("Recibir 30,000 Yuans - 20$",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        //startInAppPurchase("3_dollar_donation");
                        interfazPurchase.purchase(SKU_DONATION_20, (Activity) context);
                    }
                });

        builder.create().show();
    }
}
