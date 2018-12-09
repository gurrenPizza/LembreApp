package com.ifrj.tcc.lembre.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ifrj.tcc.lembre.Entidades.Baralhos;
import com.ifrj.tcc.lembre.R;

import java.util.ArrayList;

public class BaralhosAdapter extends ArrayAdapter<Baralhos> {

    private ArrayList<Baralhos> baralho;
    private Context context;

    public BaralhosAdapter(Context c, ArrayList<Baralhos> objects) {
        super(c, 0, objects);
        this.context = c;
        this.baralho = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (baralho != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_baralhos, parent, false);

            TextView txtNomeBaralho = (TextView) view.findViewById(R.id.txtNomeBaralho);
            TextView txtDescBaralho = (TextView) view.findViewById(R.id.txtDescBaralho);
            TextView txtCategoriaBaralho = (TextView) view.findViewById(R.id.txtCategoriaBaralho);

            Baralhos baralhos = baralho.get(position);

            txtNomeBaralho.setText(baralhos.getTitulo());
            txtDescBaralho.setText(baralhos.getDescricao());
            txtCategoriaBaralho.setText(baralhos.getCategoria());
        }

        return view;
    }
}
