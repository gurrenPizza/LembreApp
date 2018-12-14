package com.ifrj.tcc.lembre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ifrj.tcc.lembre.R;
import com.ifrj.tcc.lembre.entidades.Baralhos;
import com.ifrj.tcc.lembre.entidades.Cartas;

import java.util.ArrayList;

public class CartasAdapter extends ArrayAdapter<Cartas> {

    private ArrayList<Cartas> cartas;
    private Context context;

    public CartasAdapter(Context c, ArrayList<Cartas> objects) {
        super(c, 0, objects);
        this.context = c;
        this.cartas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (cartas != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.cartas_pequenas, parent, false);

            TextView txtFrente = (TextView) view.findViewById(R.id.txtFrenteCartaPequena);
            TextView txtVerso = (TextView) view.findViewById(R.id.txtVersoCartaPequena);

            Cartas carta = cartas.get(position);

            txtFrente.setText(carta.getFrente());
            txtVerso.setText(carta.getVerso());

        }
        return view;
    }

}
