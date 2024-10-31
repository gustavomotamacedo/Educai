package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.wecti.educai.R;

public class MenuActivity extends AppCompatActivity {

    private Button btnComecar, btnTutorial;
    private ProgressBar usernameProgressBar;
    private FirebaseAuth myAuth;
    private DatabaseReference userDbReference;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myAuth = FirebaseAuth.getInstance();
        userDbReference = FirebaseDatabase.getInstance().getReference("usuarios");
        intent = new Intent(this, ConteudosActivity.class);
        btnComecar = findViewById(R.id.btnComecar);
        btnTutorial = findViewById(R.id.btnTutorial);
        usernameProgressBar = findViewById(R.id.usernameProgressBar);

        usernameProgressBar.setVisibility(View.VISIBLE);
        userDbReference.child(myAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String username = String.valueOf(task.getResult().child("nomeCompleto").getValue(String.class));
                    intent.putExtra("username", username);
                    btnComecar.setVisibility(View.VISIBLE);
                    btnTutorial.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MenuActivity.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                }
                usernameProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        btnComecar.setOnClickListener(v -> {
            startActivity(intent);
        });
    }
}