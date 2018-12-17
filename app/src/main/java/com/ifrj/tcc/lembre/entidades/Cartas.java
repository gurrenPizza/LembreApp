package com.ifrj.tcc.lembre.entidades;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;
import com.ifrj.tcc.lembre.DAO.ConfiguracaoFirebase;

public class Cartas {

    private String frente;
    private String verso;
    private Integer idCarta;

    public Cartas() {
    }

    public Cartas(String frente) {
        this.frente = frente;
    }

    public void salvarCarta(final String tituloBaralho, final Context c) {

        try {
             final DatabaseReference cartaRef = ConfiguracaoFirebase.getFirebase().child("baralhos")
                    .child(tituloBaralho);
             cartaRef.addValueEventListener(new ValueEventListener() {
                 Integer quantCartas;
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         Baralhos baralho = dataSnapshot.getValue(Baralhos.class);
                         Toast.makeText(c, String.valueOf(baralho.getQuantCartas()+4), Toast.LENGTH_SHORT).show();
                         quantCartas = baralho.getQuantCartas();
                         if(quantCartas==0)
                             idCarta=0;
                         else
                             idCarta=quantCartas+1;
                     }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
             cartaRef.child("cartas").child(String.valueOf(idCarta)).setValue(this);
             cartaRef.child("quantCartas").setValue(String.valueOf(idCarta+1));


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getFrente() {
        return frente;
    }

    public void setFrente(String frente) {
        this.frente = frente;
    }

    public String getVerso() {
        return verso;
    }

    public void setVerso(String verso) {
        this.verso = verso;
    }
}
