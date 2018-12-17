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

import java.util.HashMap;
import java.util.Map;

public class Cartas {

    private String frente;
    private String verso;
    private int idCarta;

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
                         Toast.makeText(c, String.valueOf(baralho.getQuantCartas()), Toast.LENGTH_SHORT).show();
                         quantCartas = baralho.getQuantCartas();
                         if(quantCartas==0) {
                             setIdCarta(0);
                         }
                         else {
                             setIdCarta(quantCartas + 1);
                         }
                     }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

        }catch (Exception e){
            e.printStackTrace();
        }
        DatabaseReference cartaRef = ConfiguracaoFirebase.getFirebase().child("baralhos")
                .child(tituloBaralho);

        cartaRef.child("cartas").child(String.valueOf(getIdCarta())).setValue(this);
        Toast.makeText(c, getIdCarta(), Toast.LENGTH_SHORT).show();
        cartaRef.child("quantCartas").setValue(String.valueOf(getIdCarta()+1));
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapCarta = new HashMap<>();
        hashMapCarta.put("frente", getFrente());
        hashMapCarta.put("verso", getVerso());
        hashMapCarta.put("idCarta", getIdCarta());

        return hashMapCarta;
    }

    public Integer getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(Integer idCarta) {
        this.idCarta = idCarta;
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
