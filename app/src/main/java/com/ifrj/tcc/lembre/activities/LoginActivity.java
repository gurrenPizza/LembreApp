package com.ifrj.tcc.lembre.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.entidades.Usuarios;
import com.ifrj.tcc.lembre.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private TextView txtCadastrar, txtEsqueceuSenha;
    private Button btnLogin;
    private FirebaseAuth autenticacao;
    private Usuarios usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtLoginEmail);
        edtSenha = (EditText) findViewById(R.id.edtLoginSenha);
        btnLogin = (Button) findViewById(R.id.btnLogar);
        txtCadastrar = (TextView) findViewById(R.id.txtCadastrar);
        txtEsqueceuSenha = (TextView) findViewById(R.id.txtEsqueceuSenha);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")){
                    usuario = new Usuarios();

                    usuario.setEmail(edtEmail.getText().toString().trim());
                    usuario.setSenha(edtSenha.getText().toString());

                    validarLogin();
                }else{
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastro();
            }
        });

        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaEsqueceuSenha();
            }
        });
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent abrirTelaPrincipal = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(abrirTelaPrincipal);
        finish();
    }

    private void abrirTelaEsqueceuSenha() {
        Intent abrirTelaEsqueciSenha = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
        startActivity(abrirTelaEsqueciSenha);
        finish();
    }

    public void abrirTelaCadastro(){
        Intent abrirTelaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(abrirTelaCadastro);
        finish();
    }


}
