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

import java.text.Format;
import java.util.Objects;

public class UserModel {
    private String id;
    private String nomeCompleto;
    private String email;
    private int moedas;

    public UserModel() {
        this.moedas = 0;
    }

    public UserModel(String id, String nomeCompleto, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.moedas = 0;
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

    public int getMoedas() {
        return moedas;
    }

    public void salvar() {
        DatabaseReference usuariosReference = FirebaseDatabase.getInstance().getReference("usuarios");
        usuariosReference.child(getId()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference trilhasReference = FirebaseDatabase.getInstance().getReference("trilhas");
                    DatabaseReference lojaReference = FirebaseDatabase.getInstance().getReference("loja");
                    DatabaseReference configuracoesRef = FirebaseDatabase.getInstance().getReference("configuracoes");
                    trilhasReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot trilhaSnapshot : snapshot.getChildren()) {
                                    Object trilha = trilhaSnapshot.getValue();
                                    usuariosReference.child(getId()).child("trilhas").child(trilhaSnapshot.getKey()).setValue(trilha).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                    lojaReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot lojaSnapshot : snapshot.getChildren()) {
                                    Object loja = lojaSnapshot.getValue();
                                    usuariosReference.child(getId()).child("loja").child(lojaSnapshot.getKey()).setValue(loja).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Log.d("Firebase", "Loja " + lojaSnapshot.getKey() + " copiada para o usuario " + getId());
                                            else
                                                Log.e("Firebase", "Erro ao copiar a loja " + lojaSnapshot.getKey(), task.getException());
                                        }
                                    });
                                }
                            } else {
                                Log.e("Firebase", "Nenhuma loja encontrada para copiar");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Erro ao ler trilhas: " + error.getMessage());
                        }
                    });

                    configuracoesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot configSnapshot : snapshot.getChildren()) {
                                    Object config = configSnapshot.getValue();
                                    usuariosReference.child(getId()).child("configuracoes").child(Objects.requireNonNull(configSnapshot.getKey())).setValue(config).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Firebase", "Loja " + configSnapshot.getKey() + " copiada para usuario " + getId());
                                            } else {
                                                Log.e("Firebase", "Erro ao copiar a config" + configSnapshot.getKey(), task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Erro ao ler configuracoes : " + error.getMessage());
                        }
                    });
                } else {
                    Log.e("Firebase", "Erro ao salvar usuário: ", task.getException());
                }
            }
        });
    }
}
