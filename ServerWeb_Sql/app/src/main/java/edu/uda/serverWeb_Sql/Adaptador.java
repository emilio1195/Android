package edu.uda.serverWeb_Sql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter{

    private Context mContext;
    private ArrayList<DatosBanda> mList;
    public Adaptador(Context context, ArrayList<DatosBanda> list) {
        super  (context, R.layout.fragment, list);
        mContext= context;
        mList= list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment,null);
        }
        else
        {
            view = convertView;
        }
        TextView cod = (TextView) view.findViewById(R.id.cod);
        TextView nom = (TextView) view.findViewById(R.id.nom);
        TextView tipo = (TextView) view.findViewById(R.id.gener);

        cod.setText(mList.get(position).getCodigo());
        nom.setText(mList.get(position).getNombre());
        tipo.setText(mList.get(position).getTipo());

        return
                view;
    }
}

