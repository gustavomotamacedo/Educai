package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import edu.wecti.educai.model.AssuntoAdapter;
import edu.wecti.educai.model.AssuntoModel;

public class RoadmapActivity extends AppCompatActivity {

    private TextView txtNome, txtMoedas;
    private ImageView imgMoeda;
    private String username;
    private RecyclerView rcvRoadmap;
    private String trilhaAtual;
    private ArrayList<AssuntoModel> assuntoModelArrayList;
    private DatabaseReference trilhasRef, usuariosRef;
    private DatabaseReference configuracoesRef;
    private List<TextView> todasTextViews = new ArrayList<>();
    private FirebaseAuth myAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_roadmap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myAuth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(myAuth.getUid());
        configuracoesRef = usuariosRef.child("configuracoes");
        trilhasRef = usuariosRef.child("trilhas");

        Intent in = getIntent();
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        txtMoedas = findViewById(R.id.txtMoedas);
        imgMoeda = findViewById(R.id.imgMoeda);
        rcvRoadmap = findViewById(R.id.rcvRoadmap);
        progressBar = findViewById(R.id.progressBar);
        trilhaAtual = in.getStringExtra("trilha");
        assuntoModelArrayList = new ArrayList<>();

        txtNome.setText(username);

        configuracoesRef.child("tamanho da fonte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    float fontSize = Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                    txtNome.setTextSize(fontSize*2.0F);
                    for (TextView textView : todasTextViews) {
                        textView.setTextSize(fontSize);
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

        trilhasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                assuntoModelArrayList.clear();
                DataSnapshot result = snapshot.child(trilhaAtual);
                for (DataSnapshot assunto : result.getChildren()) {
                    AssuntoModel model = new AssuntoModel();
                    model.setNome(assunto.getKey());
                    model.setCompletado(Boolean.parseBoolean(String.valueOf(assunto.child("completada").getValue())));
                    model.setResumo(String.valueOf(assunto.child("resumo").getValue()));
                    model.setArtigoLink(String.valueOf(assunto.child("artigo").getValue()));
                    model.setVideoLink(String.valueOf(assunto.child("video").getValue()));
                    assuntoModelArrayList.add(model);
                }
                rcvRoadmap.setLayoutManager(new LinearLayoutManager(RoadmapActivity.this));
                rcvRoadmap.setAdapter(new AssuntoAdapter(RoadmapActivity.this, assuntoModelArrayList, trilhaAtual, username));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    private void injetarNaRecyclerViewComAtraso() {
        // Cria um Handler associado à Thread principal
        Handler handler = new Handler();

        // Define o atraso em milissegundos (por exemplo, 3 segundos)
        int atraso = 800; // 3000 ms = 3 segundos

        // Agendar a execução do código após o atraso
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Código a ser executado após o atraso
                rcvRoadmap.setLayoutManager(new LinearLayoutManager(RoadmapActivity.this));
                rcvRoadmap.setAdapter(new AssuntoAdapter(RoadmapActivity.this, assuntoModelArrayList, trilhaAtual, username));
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, atraso);
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