package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.wecti.educai.R;

public class AssuntoActivity extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtResumo;
    private TextView txtArtigo;
    private TextView txtVideo;
    private String username;
    private DatabaseReference databaseReference;
    private String trilhaAtual;
    private String assuntoAtual;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("trilhas");

        Intent in = getIntent();
        trilhaAtual = in.getStringExtra("trilha");
        assuntoAtual = in.getStringExtra("assunto");
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        txtArtigo = findViewById(R.id.txtArtigo);
        txtResumo = findViewById(R.id.txtResumo);
        txtVideo = findViewById(R.id.txtVideo);

        databaseReference.child(trilhaAtual).child(assuntoAtual).child("completada").setValue("true");

        databaseReference.child(trilhaAtual).child(assuntoAtual).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

    }
}