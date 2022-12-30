package ec.blcode.stickerswapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StickersListCartoon_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StickersListCartoon_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StickersListCartoon_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StickersListCartoon_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StickersListCartoon_Fragment newInstance(String param1, String param2) {
        StickersListCartoon_Fragment fragment = new StickersListCartoon_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stickers_list_cartoon_, container, false);
    }
}

/*

implements InterfaceViewStickerStatics {

    ArrayList<DataSticker> ListaStickers;
    private RecyclerView recyclerView;
    private InterfacePresenterStickerStatics IFPresenterStickersStatics;


    int cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__stickers_pack);
        recyclerView = (RecyclerView) findViewById(R.id.rvViewStickers);
        IFPresenterStickersStatics = new Presenter_StickersListStatics(this, getActivity());
        cantidad=IFPresenterStickersStatics.numberItemsShowScreen(getActivity());
        initRecyclerView();
    }

    @Override
    public void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),cantidad);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        // que formara el Recycler view en la actividad
        recyclerView.setLayoutManager(gridLayoutManager); //configuramos que el recycler view obtenga todas las caracteristicas de un LinearLayout
    }

    @Override
    public AdapterListaStickersBuy initAdapter(ArrayList<DataSticker> ListStickersStaticsBuy) {
        AdapterListaStickersBuy adapterListaStickersBuy = new AdapterListaStickersBuy(ListStickersStaticsBuy,getActivity());
        return adapterListaStickersBuy;
    }

    @Override
    public void startAdapter(AdapterListaStickersBuy adapterListaStickersBuy) {
        recyclerView.setAdapter(adapterListaStickersBuy);
    }

 */