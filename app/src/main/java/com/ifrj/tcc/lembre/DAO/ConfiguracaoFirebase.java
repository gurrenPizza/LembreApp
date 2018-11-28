package com.ifrj.tcc.lembre.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    public static DatabaseReference referenciaFirebase;
    public static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){
        // Aqui o programa verifica se a referencia existe, e se não existir, ela dá um
        // VRAU no Firebase e arranja uma
        if(referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;

    }

    public static FirebaseAuth getFirebaseAutenticacao(){

        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
