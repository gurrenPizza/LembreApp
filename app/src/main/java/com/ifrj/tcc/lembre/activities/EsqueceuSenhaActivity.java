package com.ifrj.tcc.lembre.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.R;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private Button btnLembrar;
    private Toolbar tbEsqueci;
    private EditText edtEmailEsqueciSenha;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        tbEsqueci = (Toolbar) findViewById(R.id.tbEsqueci);
        setSupportActionBar(tbEsqueci);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbEsqueci.setPadding(0, getStatusBarHeight(), 0, 0);

        edtEmailEsqueciSenha = (EditText) findViewById(R.id.edtEmailEsqueciSenha);

        btnLembrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edtEmailEsqueciSenha.getText().toString().equals("")) {
                    auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

                    auth.sendPasswordResetEmail(edtEmailEsqueciSenha.getText().toString());
                }else{
                    Toast.makeText(EsqueceuSenhaActivity.this, "Preencha o campo de email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(EsqueceuSenhaActivity.this, LoginActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
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

}
