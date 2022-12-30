package ec.blcode.stickerswapp.Functions.Archivers;

import android.content.Context;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readYuanes;
import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.writeYuanes;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_YUANES;

public class YuanesArchiver {
    private static int YuanVal;

    public static int getYuanVal(Context context){
        readYuan(context);
        return YuanVal;
    }

    public static void readYuan(Context context){

        int val = readYuanes(context, KEY_YUANES);
        if (val < 0) //obtiene un valor menor a cero cuando no existe el dato o el archivo prefsahre
            YuanVal = 0;
        else
            YuanVal = val;

    }


    public static boolean payYuan(int CostoSticker, Context context){
        readYuan(context);
        int diff;
        if (YuanVal >= CostoSticker) {
            diff = YuanVal - CostoSticker;
            writeYuanes(context, KEY_YUANES, diff);
            return true;
        }else {
            return false;
        }
    }

    public static void giftYuanes(Context context, int Valgift){
        if (YuanVal == 0)
            readYuan(context);
        int salarioNew = YuanVal +  Valgift;
        writeYuanes(context, KEY_YUANES, salarioNew);
    }
}
