package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.wecti.educai.R;

public class AssuntoActivity extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtMoedas;
    private TextView txtResumo;
    private TextView txtArtigo;
    private TextView txtVideo;
    private ImageView imgMoeda;
    private String username;
    private DatabaseReference tilhasRef, usuariosRef;
    private DatabaseReference configuracoesRef;
    private FirebaseAuth myAuth;
    private String trilhaAtual;
    private String assuntoAtual;
    private List<TextView> todasTextViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resumo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myAuth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(myAuth.getUid());
        tilhasRef = usuariosRef.child("trilhas");
        configuracoesRef = usuariosRef.child("configuracoes");

        Intent in = getIntent();
        trilhaAtual = in.getStringExtra("trilha");
        assuntoAtual = in.getStringExtra("assunto");
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        txtArtigo = findViewById(R.id.txtArtigo);
        txtResumo = findViewById(R.id.txtResumo);
        txtVideo = findViewById(R.id.txtVideo);
        txtMoedas = findViewById(R.id.txtMoedas);
        imgMoeda = findViewById(R.id.imgMoeda);
        View rootView = findViewById(R.id.main);

        encontrarTodasAsTextViews(rootView);

        tilhasRef.child(trilhaAtual).child(assuntoAtual).child("completada").setValue("true");

        configuracoesRef.child("tamanho da fonte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (TextView textView : todasTextViews) {
                        float fontSize = Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                        textView.setTextSize(fontSize);
                        txtNome.setTextSize(fontSize*2.0F);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Erro ao buscar configurações adicionais", error.toException());
            }
        });


        usuariosRef.child("moedas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtMoedas.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tilhasRef.child(trilhaAtual).child(assuntoAtual).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot assunto = task.getResult();
                    txtVideo.setText(String.valueOf(assunto.child("video").getValue()));
                    txtResumo.setText(String.valueOf(assunto.child("resumo").getValue()));
                    txtArtigo.setText(String.valueOf(assunto.child("artigo").getValue()));
                    Log.d("firebase video", assunto.child("video").getValue().toString());
                    Log.d("firebase", task.getResult().toString());
                } else {
                    Log.d("firebase", task.getException().getMessage());
                }
            }
        });

        txtNome.setText(username);
        txtArtigo.setMovementMethod(LinkMovementMethod.getInstance());
        txtVideo.setMovementMethod(LinkMovementMethod.getInstance());

        imgMoeda.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LojaActivity.class);
//            intent.putExtra("moedas", String.valueOf(moedas));
//            startActivity(intent);
        });

        txtNome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConfigActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
    private void encontrarTodasAsTextViews(@NonNull View view) {
        if (view instanceof TextView) {
            todasTextViews.add((TextView) view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                encontrarTodasAsTextViews(child);
            }
        }
    }
}