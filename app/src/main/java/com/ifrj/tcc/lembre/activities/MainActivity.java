package com.ifrj.tcc.lembre.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ifrj.tcc.lembre.adapter.BaralhosAdapter;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;
import com.ifrj.tcc.lembre.constantes.CONSTANTS;
import com.ifrj.tcc.lembre.entidades.Baralhos;
import com.ifrj.tcc.lembre.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ArrayList<Baralhos> baralhos;
    private ArrayAdapter<Baralhos> adapter;
    private Baralhos abrirBaralho;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerBaralhos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddBaralho);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCriarBaralho();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        baralhos = new ArrayList<>();

        listView = findViewById(R.id.lvPrincipal);
        adapter = new BaralhosAdapter(this,baralhos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //o adapter capta as informações do item e converte para um objeto Baralho
                //assim, é armazenado no objeto "abrirBaralho", onde suas informações podem ser manipuladas
                abrirBaralho = adapter.getItem(i);
                abrirTelaBaralho(abrirBaralho.getTitulo(), abrirBaralho.getDescricao(), abrirBaralho.getCategoria());
            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("baralhos");

        valueEventListenerBaralhos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                baralhos.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Baralhos baralhosNovos = dados.getValue(Baralhos.class);

                    baralhos.add(baralhosNovos);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

    } //fim onCreate

    private void abrirTelaBaralho(String tituloBaralho, String descBaralho, String categoriaBaralho) {
        startActivity(new Intent(this, BaralhoActivity.class)
                .putExtra(CONSTANTS.TITULO_BARALHO, tituloBaralho)
                .putExtra(CONSTANTS.CATEGORIA_BARALHO, categoriaBaralho)
                .putExtra(CONSTANTS.DESC_BARALHO, descBaralho));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerBaralhos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerBaralhos);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_baralhos) {
            Toast.makeText(MainActivity.this, "Essa funcionalidade ainda não está disponível :(", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_favoritos) {
            Toast.makeText(MainActivity.this, "Essa funcionalidade ainda não está disponível :(", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_feedback) {
            abrirTelaFeedback();
        } else if (id == R.id.nav_sobre) {
            abrirTelaSobre();
        } else if (id == R.id.nav_sair) {
            abrirTelaLogin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void abrirTelaLogin() {
        Intent abrirTelaLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(abrirTelaLogin);
        finish();
    }

    private void abrirTelaBaralhos() {
        Intent abrirTelaBaralho = new Intent(MainActivity.this, BaralhosUsuarioActivity.class);
        startActivity(abrirTelaBaralho);
        finish();
    }

    private void abrirTelaSobre() {

    }

    private void abrirTelaFeedback() {
        Intent abrirTelaFeedback = new Intent(MainActivity.this, FeedbackActivity.class);
        startActivity(abrirTelaFeedback);
        finish();
    }

    private void abrirCriarBaralho(){
        Intent abrirTelaCriarBaralho = new Intent(MainActivity.this, CadastroBaralhoActivity.class);
        startActivity(abrirTelaCriarBaralho);
        finish();
    }

}
