package com.ifrj.tcc.lembre.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.Entidades.Usuarios;
import com.ifrj.tcc.lembre.Helper.Base64Custom;
import com.ifrj.tcc.lembre.Helper.Preferencias;
import com.ifrj.tcc.lembre.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtCadEmail,
                     edtCadNome,
                     edtCadNickname,
                     edtCadSenha,
                     edtCadConfirmaSenha,
                     edtCadAniversario;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private Button btnGravar;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;
    private FirebaseUser firebaseUser;
    private DatabaseReference referencia;
    private android.support.v7.widget.Toolbar tbCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        tbCadastro = (android.support.v7.widget.Toolbar) findViewById(R.id.tbCadastro);
        setSupportActionBar(tbCadastro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbCadastro.setPadding(0, getStatusBarHeight(), 0, 0);


        //definindo a representação dos elementos da interface (XML) para manipulação na
        //programação (JAVA)
        edtCadEmail = (EditText) findViewById(R.id.edtCadEmail);
        edtCadNome = (EditText) findViewById(R.id.edtCadNome);
        edtCadNickname = (EditText) findViewById(R.id.edtCadNickname);
        edtCadSenha = (EditText) findViewById(R.id.edtCadSenha);
        edtCadConfirmaSenha = (EditText) findViewById(R.id.edtCadConfirmaSenha);
        edtCadAniversario = (EditText) findViewById(R.id.edtCadAniversario);
        rbFeminino = (RadioButton) findViewById(R.id.rbCadFeminino);
        rbMasculino = (RadioButton) findViewById(R.id.rbCadMasculino);
        btnGravar = (Button) findViewById(R.id.btnGravar);
        tbCadastro = (android.support.v7.widget.Toolbar) findViewById(R.id.tbCadastro);

        btnGravar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (edtCadSenha.getText().toString().equals(edtCadConfirmaSenha.getText().toString())) {

                    usuarios = new Usuarios();
                    //recupera todos os valores nos campos preenchidos na tela pelo usuário
                    //e armazena no objeto de usuário
                    usuarios.setNome(edtCadNome.getText().toString());
                    usuarios.setNickname(edtCadNickname.getText().toString());
                    usuarios.setEmail(edtCadEmail.getText().toString());
                    usuarios.setSenha(edtCadSenha.getText().toString());
                    usuarios.setAniversario(edtCadAniversario.getText().toString());
                    //faz a checagem de qual botão de radio está selecionado para preencher o campo sexo
                    if (rbFeminino.isChecked()) {
                        usuarios.setSexo("Feminino");
                        cadastrarUsuario();
                    } else if(rbMasculino.isChecked()) {
                        usuarios.setSexo("Masculino");
                        cadastrarUsuario();
                    }
                    else{
                        Toast.makeText(CadastroActivity.this, "Preencha todos os campos!",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(CadastroActivity.this, "As senhas não são correspondentes",Toast.LENGTH_LONG).show();
                }
            }
        });

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

    //efetua o cadastro de usuários com e-mail e senha, além de criar o nó próprio do usuário no
    //realtime database
    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com MUITO sucesso!",Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarString(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();
                }
                else{
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        erroExcecao = "digite uma senha mais forte com ao menos 8 caracteres contendo letras e números";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "o e-mail digitado é inválido, digite um novo e-mail";
                    }catch(FirebaseAuthUserCollisionException e){
                        erroExcecao = "esse e-mail já está cadastrado no sistema";
                    }catch(Exception e){
                        erroExcecao = "erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao,Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(CadastroActivity.this, LoginActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    public void abrirLoginUsuario(){
        Intent abrirTelaLogin = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(abrirTelaLogin);
        finish();
    }
}
