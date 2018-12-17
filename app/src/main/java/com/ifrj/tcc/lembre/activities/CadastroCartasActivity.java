package com.ifrj.tcc.lembre.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.R;
import com.ifrj.tcc.lembre.constantes.CONSTANTS;
import com.ifrj.tcc.lembre.entidades.Baralhos;
import com.ifrj.tcc.lembre.entidades.Cartas;

public class CadastroCartasActivity extends AppCompatActivity {

    private EditText edtCadFrenteCarta,
                     edtCadVersoCarta;
    private Button btnCadCarta;
    private android.support.v7.widget.Toolbar tbCadCarta;
    private DatabaseReference baralhoRef;
    private Cartas carta;
    private Baralhos baralho;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cartas);

        //relativo à toolbar
        tbCadCarta = (android.support.v7.widget.Toolbar) findViewById(R.id.tbCadCartas);
        setSupportActionBar(tbCadCarta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbCadCarta.setPadding(0, getStatusBarHeight(), 0, 0);

        baralho = new Baralhos();
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            baralho.setTitulo(extras.getString(CONSTANTS.TITULO_BARALHO));
            baralho.setCategoria(extras.getString(CONSTANTS.CATEGORIA_BARALHO));
            baralho.setDescricao(extras.getString(CONSTANTS.DESC_BARALHO));
        }

        edtCadFrenteCarta = (EditText) findViewById(R.id.edtCadFrenteCarta);
        edtCadVersoCarta = (EditText) findViewById(R.id.edtCadVersoCarta);
        btnCadCarta = (Button) findViewById(R.id.btnCadastrarCarta);

        btnCadCarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edtCadFrenteCarta.getText().toString().equals("")||!edtCadVersoCarta.getText().toString().equals("")){
                    cadastrarCarta(edtCadFrenteCarta.getText().toString().trim(),
                                   edtCadVersoCarta.getText().toString().trim(),
                                   baralho.getTitulo());
                }else{
                    Toast.makeText(CadastroCartasActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //conectar com o banco e cadastrar no baralho
    private void cadastrarCarta(String frenteCarta, String versoCarta, String tituloBaralho) {
        carta = new Cartas();

        carta.setFrente(frenteCarta);
        carta.setVerso(versoCarta);
        carta.salvarCarta(tituloBaralho, CadastroCartasActivity.this);
        Toast.makeText(this, "Carta adicionada com sucesso!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja cadastrar outra carta?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtCadFrenteCarta.setText("");
                edtCadVersoCarta.setText("");
            }
        });
        builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int i) {
                abrirTelaBaralho();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    private int getStatusBarHeight() {
        int height;

        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier(
                "status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
        }else{
            height = 0;
            Toast.makeText(this,
                    "Resources NOT found",
                    Toast.LENGTH_LONG).show();
        }
        return height;
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        startActivity(new Intent(this, BaralhoActivity.class)
                .putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo())
                .putExtra(CONSTANTS.CATEGORIA_BARALHO, baralho.getCategoria())
                .putExtra(CONSTANTS.DESC_BARALHO, baralho.getDescricao())); //O efeito ao ser pressionado do botão (no caso abre a activity)

        finishAffinity(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, BaralhoActivity.class)
                        .putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo())
                        .putExtra(CONSTANTS.CATEGORIA_BARALHO, baralho.getCategoria())
                        .putExtra(CONSTANTS.DESC_BARALHO, baralho.getDescricao()));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    public void abrirTelaBaralho(){
        startActivity(new Intent(this, BaralhoActivity.class)
                .putExtra(CONSTANTS.TITULO_BARALHO, baralho.getTitulo())
                .putExtra(CONSTANTS.CATEGORIA_BARALHO, baralho.getCategoria())
                .putExtra(CONSTANTS.DESC_BARALHO, baralho.getDescricao()));
        finishAffinity();
    }

}
