package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.wecti.educai.R;
import edu.wecti.educai.model.AssuntoAdapter;
import edu.wecti.educai.model.AssuntoModel;
import edu.wecti.educai.model.TrilhasAdapter;

public class RoadmapActivity extends AppCompatActivity {

    private TextView txtNome, txtMoedas;
    private ImageView imgMoeda;
    private String username;
    private RecyclerView rcvRoadmap;
    private String trilhaAtual;
    private ArrayList<AssuntoModel> assuntoModelArrayList;
    private DatabaseReference dbReference, usuariosRef;
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
        dbReference = usuariosRef.child("trilhas");

        Intent in = getIntent();
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        txtMoedas = findViewById(R.id.txtMoedas);
        imgMoeda = findViewById(R.id.imgMoeda);
        rcvRoadmap = findViewById(R.id.rcvRoadmap);
        progressBar = findViewById(R.id.progressBar);
        trilhaAtual = in.getStringExtra("trilha");
        Log.d("firebase", trilhaAtual);
        assuntoModelArrayList = new ArrayList<>();

        txtNome.setText(username);

        usuariosRef.child("moedas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtMoedas.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbReference.addValueEventListener(new ValueEventListener() {
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
}