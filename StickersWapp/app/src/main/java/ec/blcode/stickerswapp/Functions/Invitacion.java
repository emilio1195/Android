package ec.blcode.stickerswapp.Functions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_UUID;

public class Invitacion {
    static Uri mInvitationUrl;
    public static void Invitacion(Context context){
        String uid = readDataAppText(context, KEY_UUID);
        String link = "https://stickerswapp.page.link/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://stickerswapp.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder(context.getPackageName())
                                .setMinimumVersion(19)
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = shortDynamicLink.getShortLink();
                        sendInvitation(context);
                    }
                });
    }

    private static void sendInvitation(Context context){
        String subject = ("Quiero ganar monedas invitandote a esta App de Android");
        String invitationLink = mInvitationUrl.toString();
        String msg = "Es una nueva Plataforma dinámica de stickers para Whats App, que funciona meidante monedas, " +
                        "esta muy divertido su contenido. Descárgala solo en Play Store con el siguiente Link: \n"
                + invitationLink;
        String msgHtml = String.format("<p> Es una nueva Plataforma dinámica de stickers para Whats App, que funciona meidante monedas, "+
                "esta muy divertido su contenido. Descárgala solo en Play Store con el siguiente Link: "
                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }



}
