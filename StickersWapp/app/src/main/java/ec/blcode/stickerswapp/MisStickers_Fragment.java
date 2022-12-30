package ec.blcode.stickerswapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import ec.blcode.stickerswapp.Functions.Dialogos.SetProgressDialog;

public class MisStickers_Fragment extends Fragment {

    Button btnMisStickers;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MisStickers_Fragment() {
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
    public static MisStickers_Fragment newInstance(String param1, String param2) {
        MisStickers_Fragment fragment = new MisStickers_Fragment();
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
        View v = inflater.inflate(R.layout.fragment_mis_stickers_, container, false);
        btnMisStickers = (Button) v.findViewById(R.id.btnDescargarStickers);
        clicBtnDescargarStickers();
        return v;
    }

    private void clicBtnDescargarStickers(){
        btnMisStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetProgressDialog setProgressDialog = new SetProgressDialog(getActivity());
                setProgressDialog.getAlertDialog("AVISO","Esta función estará lista próximamente.\n" + "Disculpa las molestias.");
            }
        });
    }
}