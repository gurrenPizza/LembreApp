package com.ifrj.tcc.lembre.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.R;
import com.ifrj.tcc.lembre.adapter.CartasAdapter;
import com.ifrj.tcc.lembre.constantes.CONSTANTS;
import com.ifrj.tcc.lembre.entidades.Baralhos;
import com.ifrj.tcc.lembre.entidades.Cartas;

import java.util.ArrayList;

public class BaralhoActivity extends AppCompatActivity {

    private TextView txtTituloBaralho,
                     txtDescBaralho,
                     txtCategoriaBaralho;
    private Toolbar tbBaralho;

    private DatabaseReference baralhoRef;
    private Baralhos baralho;
    private ArrayList<Cartas> arrayCartas;
    private ListView lvCartas;
    private ArrayAdapter<Cartas> adapter;
    private ValueEventListener valueEventListenerCartas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baralho);

        //relacionado à action bar
        tbBaralho = (Toolbar) findViewById(R.id.tbBaralho);
        setSupportActionBar(tbBaralho);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbBaralho.setPadding(0, getStatusBarHeight(), 0, 0);

        //Identifica os campos de textos que vão ser preenchidos com o Bundle
        txtTituloBaralho = (TextView) findViewById(R.id.txtTituloBaralho);
        txtCategoriaBaralho = (TextView) findViewById(R.id.txtCategoriaBaralho);
        txtDescBaralho = (TextView) findViewById(R.id.txtDescBaralho);

        baralho = new Baralhos();
        //recupera os valores passados pelas activities anteriores.
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            baralho.setTitulo(extras.getString(CONSTANTS.TITULO_BARALHO));
            baralho.setDescricao(extras.getString(CONSTANTS.DESC_BARALHO));
            baralho.setCategoria(extras.getString(CONSTANTS.CATEGORIA_BARALHO));
        }

        txtTituloBaralho.setText(baralho.getTitulo());
        txtDescBaralho.setText(baralho.getDescricao());
        txtCategoriaBaralho.setText(baralho.getCategoria());

        arrayCartas = new ArrayList<Cartas>();

        lvCartas = (ListView) findViewById(R.id.lvCartas);
        adapter = new CartasAdapter(this, arrayCartas);
        lvCartas.setAdapter(adapter);

        //a linha abaixo faz referência ao nó das cartas armazenadas no nó do baralho clicado na
        //activity anterior, que também está dentro do nó "baralhos"
        baralhoRef = ConfiguracaoFirebase.getFirebase().child("baralhos").child(extras.getString(CONSTANTS.TITULO_BARALHO)).child("cartas");

        valueEventListenerCartas = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayCartas.clear();
                arrayCartas.add(new Cartas("adicionar"));
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Cartas cartaDoBaralho = dados.getValue(Cartas.class);
                    arrayCartas.add(cartaDoBaralho);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BaralhoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPraticar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPratica();
            }
        });

    } //fim onCreate

    @Override
    protected void onStart() {
        super.onStart();
        baralhoRef.addValueEventListener(valueEventListenerCartas);
    }

    @Override
    protected void onStop() {
        super.onStop();
        baralhoRef.removeEventListener(valueEventListenerCartas);
    }

    private void abrirPratica() {
        Intent abrirPratica = new Intent(BaralhoActivity.this, PraticarBaralho.class);
        abrirPratica.putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo());
        startActivity(abrirPratica);
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, MainActivity.class)); //O efeito ao ser pressionado do botão (no caso abre a activity)
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    private int getStatusBarHeight() {
        int height;

        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
        }else{
            height = 0;
            Toast.makeText(BaralhoActivity.this,
                    "Resources NOT found",
                    Toast.LENGTH_LONG).show();
        }

        return height;
    }

}
