package edu.wecti.educai.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserModel {
    private String id;
    private String nomeCompleto;
    private String email;

    public UserModel() {
    }

    public UserModel(String id, String nomeCompleto, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void salvar() {
        DatabaseReference userDbreference = FirebaseDatabase.getInstance().getReference("usuarios");
        userDbreference.child(getId()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference trilhaDbReference = FirebaseDatabase.getInstance().getReference("trilhas");
                    trilhaDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot trilhaSnapshot : snapshot.getChildren()) {
                                    Object trilha = trilhaSnapshot.getValue();
                                    userDbreference.child(getId()).child("trilhas").child(trilhaSnapshot.getKey()).setValue(trilha).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Log.d("Firebase", "Trilha " + trilhaSnapshot.getKey() + " copiada para o usuário " + getId());
                                            else
                                                Log.e("Firebase", "Erro ao copiar a trilha " + trilhaSnapshot.getKey(), task.getException());
                                        }
                                    });
                                }
                            } else {
                                Log.w("Firebase", "Nenhuma trilha encontrada para copiar.");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Erro ao ler trilhas: " + error.getMessage());
                        }
                    });
                } else {
                    Log.e("Firebase", "Erro ao salvar usuário: ", task.getException());
                }
            }
        });
    }
}
