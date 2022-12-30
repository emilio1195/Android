package ec.blcode.stickerswapp.View.ViewPackListStatics;

import static ec.blcode.stickerswapp.Functions.Archivers.DataArchiverMain.readDataAppText;
import static ec.blcode.stickerswapp.Functions.Archivers.YuanesArchiver.getYuanVal;
import static ec.blcode.stickerswapp.Functions.Constantes.KEY_EMAIL;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import ec.blcode.stickerswapp.Adapter.AdapterListPackStatics;
import ec.blcode.stickerswapp.Functions.Admod.Admod;
import ec.blcode.stickerswapp.POJO.DataPack;
import ec.blcode.stickerswapp.Presenter.PresenterListPackStatics.Presenter_ListPack;
import ec.blcode.stickerswapp.R;

public class View_ListPacks_Fragment extends Fragment implements Interface_View_ListPackFragment {

    private RecyclerView recyclerView;
    private Presenter_ListPack presenter_listPack;
    Admod admod;
    private int numColumns;
    private GridLayoutManager gridLayoutManager;
    private AdapterListPackStatics adapterListPackStatics;
    private TextView txtSaldo, txtEmail;
    ShimmerFrameLayout shimmer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pack_list_statics_, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rvListPack);
        txtSaldo = (TextView) v.findViewById(R.id.txtSaldoMain);
        txtEmail = (TextView) v.findViewById(R.id.txtEmail);
        presenter_listPack = new Presenter_ListPack(getActivity(),this);
        txtSaldo.setText(getYuanVal(getActivity())+"");
        txtEmail.setText(readDataAppText(getActivity(), KEY_EMAIL));
        ImageButton btnUpdateYuans = (ImageButton) v.findViewById(R.id.btnUpdateYuans);
        btnUpdateYuans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSaldo.setText(getYuanVal(getActivity())+"");
            }
        });

        initRecyclerView();
        //////////////////////////////////Iniciar Banner en Fragment/////////////////////////////////////////////////////////////
        admod = new Admod(getActivity());
        admod.setBannerAdmodFragment(v, R.id.adViewBanner);
        /////////////////////////////////////////////////////////////////////////////////////////////////
        shimmer =
                (ShimmerFrameLayout) v.findViewById(R.id.shimmer_PacksPreview);
        shimmer.startShimmer(); // If auto-start is set to false
        return  v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Fragment_1>","Pausado.");
        txtSaldo.setText(getYuanVal(getActivity())+"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment_1>","Destruido.");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Fragment_1>","Stoped.");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("Fragment_1>","Vista Destruida.");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("Fragment_1>","Vista Restaurada.");
        txtSaldo.setText(getYuanVal(getActivity())+"");
    }

    @Override
    public void onResume() {
        super.onResume();
        txtSaldo.setText(getYuanVal(getActivity())+"");
        // admod.setBannerAdmodFragment(getView(), R.id.adViewBanner);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener pageLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int anchoCardView = recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.dimen_cardview_Category_preview);
            int padding = (recyclerView.getContext().getResources().getDimensionPixelSize(R.dimen.padding2));
            int wScreen = recyclerView.getWidth();
            int numColums = wScreen / anchoCardView;
            int numEspaces = numColums + 1;
            int wEspace = 0;
            int wtotal_CardViews = 0;
            int wtotal_Espaces = 0;
            float paddingDp = 0;
            while(true){
                wtotal_CardViews = anchoCardView * numColums;
                wEspace = (wScreen - wtotal_CardViews) / numEspaces;
                wtotal_Espaces = wEspace * numEspaces;
                int wTotal = wtotal_CardViews + wtotal_Espaces;
                if (wTotal <= wScreen) {
                    if (getContext() != null) {
                        paddingDp = convertPixelsToDp(wEspace);
                        int paddingNew = Math.round(paddingDp);
                        recyclerView.setPadding(paddingNew * 3, padding / 2, paddingNew, padding / 2); //se multiplica x 3 para compensar la perdida de wScreentotal
                    }
                    // Once data has been obtained, this listener is no longer needed, so remove it...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    break;
                }else {
                    numColums -= 1;
                    numEspaces = numColums + 1;
                }
            }
            setNumColumns(numColums);//esto es en px
        }
    };

    private void setNumColumns(int numColumns) {
        if (this.numColumns != numColumns) {
                gridLayoutManager.setSpanCount(numColumns);
                this.numColumns = numColumns;
                if (adapterListPackStatics != null) {
                    adapterListPackStatics.notifyDataSetChanged();
                }

        }else{
            gridLayoutManager.setSpanCount(this.numColumns);
        }
    }
    private float convertPixelsToDp(float px){
        return px / ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public void initRecyclerView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        // que formara el Recycler view en la actividad
        recyclerView.setLayoutManager(gridLayoutManager); //configuramos que el recycler view obtenga todas las caracteristicas de un LinearLayout
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(pageLayoutListener);
    }

    @Override
    public AdapterListPackStatics initAdapter(ArrayList<DataPack> ListDataPackCategory) {
        adapterListPackStatics = new AdapterListPackStatics(ListDataPackCategory, getActivity());
        return adapterListPackStatics;
    }

    @Override
    public void startAdapter(AdapterListPackStatics adapterListPackStatics) {
        recyclerView.setAdapter(adapterListPackStatics);
        shimmer.hideShimmer();
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
    }


}