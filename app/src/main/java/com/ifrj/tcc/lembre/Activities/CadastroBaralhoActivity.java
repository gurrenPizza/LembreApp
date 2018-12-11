package com.ifrj.tcc.lembre.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.Entidades.Baralhos;
import com.ifrj.tcc.lembre.Entidades.Usuarios;
import com.ifrj.tcc.lembre.Helper.Base64Custom;
import com.ifrj.tcc.lembre.R;

public class CadastroBaralhoActivity extends AppCompatActivity {

    private Toolbar tbCadastroBaralho;
    private Spinner spCategorias;
    private EditText edtCadTituloBaralho, edtCadDescBaralho;
    private Button btnCadBaralho;
    private Baralhos baralho;
    private DatabaseReference firebase;
    private FirebaseUser user;
    private Usuarios usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_baralho);

        //relacionado à action bar
        tbCadastroBaralho = (android.support.v7.widget.Toolbar) findViewById(R.id.tbCadastroBaralho);
        setSupportActionBar(tbCadastroBaralho);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbCadastroBaralho.setPadding(0, getStatusBarHeight(), 0, 0);

        spCategorias = (Spinner) findViewById(R.id.spinCategorias);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        spCategorias.setAdapter(adapter);

        edtCadTituloBaralho = findViewById(R.id.edtCadTituloBaralho);
        edtCadDescBaralho = findViewById(R.id.edtCadDescricaoBaralho);
        btnCadBaralho = findViewById(R.id.btnCadBaralho);

        btnCadBaralho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baralho = new Baralhos();
                usuario =  new Usuarios();
                String codUsuario="";
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    codUsuario = user.getEmail();
                    codUsuario = Base64Custom.codificarString(codUsuario);
                }else{
                    Toast.makeText(CadastroBaralhoActivity.this, "Usuário não está logado!",Toast.LENGTH_LONG).show();
                }

                firebase = ConfiguracaoFirebase.getFirebase().child("usuario");

                firebase.child(codUsuario).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);

                        usuario = usuarios;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CadastroBaralhoActivity.this, "Não rolou",Toast.LENGTH_LONG).show();
                    }
                });


                baralho.setTitulo(edtCadTituloBaralho.getText().toString());
                baralho.setDescricao(edtCadDescBaralho.getText().toString());
                baralho.setCategoria(spCategorias.getSelectedItem().toString());
                baralho.setAutor(usuario.getNickname());

                salvarBaralho(baralho);
            }
        });

    }

    @Override
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
            Toast.makeText(CadastroBaralhoActivity.this,
                    "Resources NOT found",
                    Toast.LENGTH_LONG).show();
        }

        return height;
    }

    private boolean salvarBaralho(Baralhos baralho){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("baralhos");
            firebase.child(baralho.getTitulo()).setValue(baralho);
            Toast.makeText(CadastroBaralhoActivity.this, "BaralhoActivity criado com sucesso!",Toast.LENGTH_LONG).show();
            abrirTelaBaralho();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void abrirTelaBaralho() {
        Intent abrirTelaBaralho = new Intent(CadastroBaralhoActivity.this, BaralhoActivity.class);
        startActivity(abrirTelaBaralho);
        finish();
    }


}
