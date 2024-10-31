package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.wecti.educai.R;
import edu.wecti.educai.model.AssuntoModel;
import edu.wecti.educai.model.TrilhaModel;
import edu.wecti.educai.model.TrilhasAdapter;

public class ConteudosActivity extends AppCompatActivity {

    private TextView txtNome, txtMoedas;
    private String username;
    private ProgressBar progressBar;
    private RecyclerView rcvConteudos;
    private ArrayList<TrilhaModel> trilhaModels;
    private ArrayList<AssuntoModel> assuntoModels;
    private DatabaseReference trilhasDbRef;
    private Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conteudos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        trilhasDbRef = FirebaseDatabase.getInstance().getReference("trilhas");


        in = getIntent();
        username = in.getStringExtra("username");
        txtNome = findViewById(R.id.txtNome);
        txtMoedas = findViewById(R.id.txtMoedas);
        rcvConteudos = findViewById(R.id.rcvConteudos);
        progressBar = findViewById(R.id.progressBar);
        trilhaModels = new ArrayList<>();
        assuntoModels = new ArrayList<>();

        txtNome.setText(username);

        trilhasDbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    DataSnapshot result = task.getResult();
                    for (DataSnapshot trilha : result.getChildren()) {
                        for (DataSnapshot assunto : trilha.getChildren()) {
                            AssuntoModel model = new AssuntoModel();
                            model.setNome(assunto.getKey());
                            model.setCompletado(Boolean.parseBoolean(String.valueOf(assunto.child("completado").getValue())));
                            model.setResumo(String.valueOf(assunto.child("resumo").getValue()));
                            model.setArtigoLink(String.valueOf(assunto.child("artigo").getValue()));
                            model.setVideoLink(String.valueOf(assunto.child("video").getValue()));
                            assuntoModels.add(model);
                        }
                        TrilhaModel model = new TrilhaModel(trilha.getKey(), assuntoModels);
                        trilhaModels.add(model);
                    }
                    rcvConteudos.setLayoutManager(new LinearLayoutManager(ConteudosActivity.this));
                    rcvConteudos.setAdapter(new TrilhasAdapter(ConteudosActivity.this, trilhaModels, username));
                } else {
                    Toast.makeText(ConteudosActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });



        txtNome.setOnClickListener(v -> {
            Intent intent = new Intent(this, RoadmapActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}