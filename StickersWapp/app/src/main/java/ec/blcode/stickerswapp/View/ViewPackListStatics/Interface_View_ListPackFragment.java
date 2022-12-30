package ec.blcode.stickerswapp.View.ViewPackListStatics;

import java.util.ArrayList;

import ec.blcode.stickerswapp.Adapter.AdapterListPackStatics;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.POJO.DataSticker;
import ec.blcode.stickerswapp.POJO.PackSticker;

public interface Interface_View_ListPackFragment {
    public void initRecyclerView();
    public AdapterListPackStatics initAdapter (ArrayList<DataPack> ListPackCategoryPreview);
    public void startAdapter(AdapterListPackStatics adapterListPackStatics);
}
