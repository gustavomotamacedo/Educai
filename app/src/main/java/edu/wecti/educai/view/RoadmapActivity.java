package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.wecti.educai.R;
import edu.wecti.educai.model.AssuntoAdapter;
import edu.wecti.educai.model.AssuntoModel;

public class RoadmapActivity extends AppCompatActivity {

    private TextView txtNome;
    private String username;
    private RecyclerView rcvRoadmap;
    private String trilhaAtual;
    private ArrayList<AssuntoModel> assuntoModelArrayList;
    private DatabaseReference dbReference;

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

        dbReference = FirebaseDatabase.getInstance().getReference("trilhas");

        Intent in = getIntent();
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        rcvRoadmap = findViewById(R.id.rcvRoadmap);
        trilhaAtual = in.getStringExtra("trilha");
        Log.d("firebase", trilhaAtual);
        assuntoModelArrayList = new ArrayList<>();

        txtNome.setText(username);

        dbReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot result = task.getResult().child(trilhaAtual);
                    for (DataSnapshot assunto : result.getChildren()) {
                        AssuntoModel model = new AssuntoModel();
                        model.setNome(assunto.getKey());
                        model.setCompletado(Boolean.parseBoolean(String.valueOf(assunto.child("completado").getValue())));
                        model.setResumo(String.valueOf(assunto.child("resumo").getValue()));
                        model.setArtigoLink(String.valueOf(assunto.child("artigo").getValue()));
                        model.setVideoLink(String.valueOf(assunto.child("video").getValue()));
                        assuntoModelArrayList.add(model);
                        Log.d("firebase", String.valueOf(assunto.getKey()));
                        Log.d("firebase", String.valueOf(assuntoModelArrayList));
                    }
                    rcvRoadmap.setLayoutManager(new LinearLayoutManager(RoadmapActivity.this));
                    rcvRoadmap.setAdapter(new AssuntoAdapter(RoadmapActivity.this, assuntoModelArrayList));
                } else {
                    Toast.makeText(RoadmapActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}