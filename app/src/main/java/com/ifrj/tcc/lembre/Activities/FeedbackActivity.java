package com.ifrj.tcc.lembre.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ifrj.tcc.lembre.Helper.Mail;
import com.ifrj.tcc.lembre.R;

public class FeedbackActivity extends AppCompatActivity {

    private Button btnMandarFeedback;
    private EditText edtFeedback;
    private Toolbar tbFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        tbFeedback = (Toolbar) findViewById(R.id.tbFeedback);
        setSupportActionBar(tbFeedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tbFeedback.setPadding(0, getStatusBarHeight(), 0, 0);

        btnMandarFeedback = (Button) findViewById(R.id.btnMandarFeedback);
        edtFeedback = (EditText) findViewById(R.id.edtFeedback);

        btnMandarFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarEmail();
            }
        });

    }

    private void enviarEmail() {

        final String nome = edtFeedback.getText().toString();
        final String email = "applembre@gmail.com";
        final String subject = "Feedback";
        final String body = nome;

        if(!isOnline()) {
            Toast.makeText(getApplicationContext(), "Não estava online para enviar e-mail!", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                Mail m = new Mail();

                String[] toArr = {email};
                m.setTo(toArr);

                m.setFrom("applembre@gmail.com"); //caso queira enviar em nome de outro
                m.setSubject(subject);
                m.setBody(body);

                try {
                    m.send();
                }
                catch(RuntimeException rex){ }//erro ignorado
                catch(Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }


            }
        }).start();
    }

    private boolean isOnline() {
            try {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return netInfo != null && netInfo.isConnectedOrConnecting();
            }
            catch(Exception ex){
                Toast.makeText(getApplicationContext(), "Erro ao verificar se estava online! (" + ex.getMessage() + ")", Toast.LENGTH_SHORT).show();
                return false;
            }
    }

    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(FeedbackActivity.this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
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
