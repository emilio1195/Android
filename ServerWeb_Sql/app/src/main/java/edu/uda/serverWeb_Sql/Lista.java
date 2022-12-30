package edu.uda.serverWeb_Sql;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Lista extends ListFragment {
    private ArrayList<DatosBanda> listafragmento;
    private static Adaptador adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    listafragmento = new ArrayList<DatosBanda>();
    adapter = new Adaptador(getActivity(), listafragmento);
    setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void  ActualizarAdaptador(ArrayList<DatosBanda> list1) {
        adapter.notifyDataSetChanged();
    }
}
