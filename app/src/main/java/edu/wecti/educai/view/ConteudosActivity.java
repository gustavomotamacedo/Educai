package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.wecti.educai.R;

public class ConteudosActivity extends AppCompatActivity {

    private TextView txtNome;
    private String username;
    private FirebaseAuth myAuth;
    private DatabaseReference myRef;

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

        myAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("trilhas");

        Intent in = getIntent();
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        txtNome.setText(username);

        txtNome.setOnClickListener(v -> {
            Intent intent = new Intent(this, RoadmapActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}