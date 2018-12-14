package com.ifrj.tcc.lembre.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.R;
import com.ifrj.tcc.lembre.constantes.CONSTANTS;
import com.ifrj.tcc.lembre.entidades.Baralhos;
import com.ifrj.tcc.lembre.entidades.Cartas;

import java.util.ArrayList;

public class PraticarBaralho extends AppCompatActivity {

    private Toolbar tbPraticar;

    private Baralhos baralho;
    private ArrayList<Cartas> cartas;
    private DatabaseReference baralhoRef;
    private ValueEventListener valueEventListenerCartas;

    private int totalCartas;
    private int acertos;
    private int erros;
    private int progresso;
    private ProgressBar pbPraticar;
    private Button btnPraticar;
    private EditText edtResposta;
    private TextView txtCarta;

    private FirebaseUser usuarioAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praticar_baralho);

        baralho = new Baralhos();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            baralho.setTitulo(extras.getString(CONSTANTS.TITULO_BARALHO));
            baralho.setCategoria(extras.getString(CONSTANTS.CATEGORIA_BARALHO));
            baralho.setDescricao(extras.getString(CONSTANTS.DESC_BARALHO));
        }

        //relacionado a action bar
        tbPraticar = (Toolbar) findViewById(R.id.tbPraticar);
        setSupportActionBar(tbPraticar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbPraticar.setPadding(0, getStatusBarHeight(), 0, 0);

        pbPraticar = (ProgressBar) findViewById(R.id.pbPraticar);

        cartas = new ArrayList<Cartas>();

        //fazer a consulta de quantas cartas tem e arnmazená-las no arraylist "cartas"

        baralhoRef = ConfiguracaoFirebase.getFirebase().child("baralhos").child(extras.getString(CONSTANTS.TITULO_BARALHO)).child("cartas");

        valueEventListenerCartas = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartas.clear();

                totalCartas = 0;
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Cartas cartaDoBaralho = dados.getValue(Cartas.class);
                    cartas.add(cartaDoBaralho);
                    totalCartas++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PraticarBaralho.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        progresso = 1;
        pbPraticar.setMax(totalCartas);

        txtCarta = (TextView) findViewById(R.id.txtCartaPratica);
        edtResposta = (EditText) findViewById(R.id.edtResposta);
        btnPraticar = (Button) findViewById(R.id.btnPraticar);


        if(cartas != null && Integer.valueOf(cartas.size())!=0){
            txtCarta.setText(cartas.get(progresso).getFrente());
        }
        else{
            Toast.makeText(this, "Você precisa cadastrar as cartas antes de jogar!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, BaralhoActivity.class)
                    .putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo())
                    .putExtra(CONSTANTS.CATEGORIA_BARALHO, baralho.getCategoria())
                    .putExtra(CONSTANTS.DESC_BARALHO, baralho.getDescricao()));
        }



        btnPraticar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progresso == totalCartas) {
                    terminarPratica();
                } else {

                    if (!edtResposta.equals("")) {
                        if (edtResposta.equals(cartas.get(progresso).getVerso())) {
                            progresso++;
                            acertos++;
                            pbPraticar.setProgress(progresso);
                            Toast.makeText(PraticarBaralho.this, "Você acertou!! :D", Toast.LENGTH_SHORT).show();
                            txtCarta.setText(cartas.get(progresso).getFrente());
                            pbPraticar.setProgress(progresso);
                        } else {
                            progresso++;
                            erros++;
                            Toast.makeText(PraticarBaralho.this, "Você errou!! :(", Toast.LENGTH_SHORT).show();
                            txtCarta.setText(cartas.get(progresso).getFrente());
                            pbPraticar.setProgress(progresso);
                        }
                    } else {
                        Toast.makeText(PraticarBaralho.this, "Vai responder não?", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }// fim onCreate

    private void terminarPratica() {

        usuarioAtual = ConfiguracaoFirebase.getFirebaseAutenticacao().getCurrentUser();
        Toast.makeText(this, "Parabéns, você ganhou 15xp! Acertou " + acertos +  " vezes e da próxima vai lembrar ainda mais!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, BaralhoActivity.class)
                .putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo())
                .putExtra(CONSTANTS.CATEGORIA_BARALHO, baralho.getCategoria())
                .putExtra(CONSTANTS.DESC_BARALHO, baralho.getDescricao()));

    }

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

    private int getStatusBarHeight() {
        int height;

        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
        } else {
            height = 0;
            Toast.makeText(this,
                    "Resources NOT found",
                    Toast.LENGTH_LONG).show();
        }

        return height;
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, BaralhoActivity.class).putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo()));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() { //Botão BACK padrão do android
        //O efeito ao ser pressionado do botão (no caso abre a activity)
        startActivity(new Intent(this, BaralhoActivity.class).putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo()));
        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

}
