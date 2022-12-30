package ec.blcode.stickerswapp.View.ViewStickerStatics;

import java.util.ArrayList;

import ec.blcode.stickerswapp.Adapter.AdapterListaStickers;
import ec.blcode.stickerswapp.POJO.DataSticker;

public interface Interface_View_DetailsPack {
    public void initRecyclerView(int i);
    public AdapterListaStickers initAdapter (ArrayList<DataSticker> ListStickersStaticsBuy);
    public void startAdapter(AdapterListaStickers adapterListaStickers);
}
