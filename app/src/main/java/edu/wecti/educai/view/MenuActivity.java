package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.wecti.educai.R;

public class MenuActivity extends AppCompatActivity {

    private Button btnComecar, btnTutorial;
    private TextView txtTitle;
    private ProgressBar usernameProgressBar;
    private FirebaseAuth myAuth;
    private DatabaseReference usuariosRef;
    private DatabaseReference configuracoesRef;
    private List<TextView> todasTextViews = new ArrayList<>();
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
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(myAuth.getUid());
        configuracoesRef = usuariosRef.child("configuracoes");

        intent = new Intent(this, ConteudosActivity.class);
        btnComecar = findViewById(R.id.btnComecar);
        btnTutorial = findViewById(R.id.btnTutorial);
        txtTitle = findViewById(R.id.txtTitle);
        usernameProgressBar = findViewById(R.id.usernameProgressBar);
        View rootView = findViewById(R.id.main);

        encontrarTodasAsTextViews(rootView);

        usernameProgressBar.setVisibility(View.VISIBLE);

        configuracoesRef.child("tamanho da fonte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (TextView textView : todasTextViews) {
                        float fontSize = Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                        textView.setTextSize(fontSize);
                        txtTitle.setTextSize(fontSize*2.0F);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Erro ao buscar configurações adicionais", error.toException());
            }
        });

        usuariosRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String username = String.valueOf(task.getResult().child("nomeCompleto").getValue(String.class));
                    intent.putExtra("username", username);
                    btnComecar.setVisibility(View.VISIBLE);
//                    btnTutorial.setVisibility(View.VISIBLE);
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