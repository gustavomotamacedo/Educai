package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class ConfigActivity extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtTamanhoAtual;
    private ImageButton btnAddFontSize;
    private ImageButton btnRemFontSize;
    private Button btnSalvar;
    private DatabaseReference configuracoesRef;
    private DatabaseReference usuariosRef;
    private FirebaseAuth myAuth;
    private List<TextView> todasTextViews = new ArrayList<>();
    private float tamAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myAuth = FirebaseAuth.getInstance();
        usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(myAuth.getUid());
        configuracoesRef = usuariosRef.child("configuracoes");

        txtNome = findViewById(R.id.txtNome);
        txtTamanhoAtual = findViewById(R.id.txtTamanhoAtual);
        btnAddFontSize = findViewById(R.id.btnAddFontSize);
        btnRemFontSize = findViewById(R.id.btnRemFontSize);
        btnSalvar = findViewById(R.id.btnSalvar);

        String username = getIntent().getStringExtra("username");
        txtNome.setText(username);
        tamAtual = 0.0F;
        View rootView = findViewById(R.id.main);

        encontrarTodasAsTextViews(rootView);

        configuracoesRef.child("tamanho da fonte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (TextView textView : todasTextViews) {
                        float fontSize = Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                        textView.setTextSize(fontSize);
                        txtNome.setTextSize(fontSize*2.0F);
                        txtTamanhoAtual.setText(String.valueOf(fontSize));
                        tamAtual = fontSize;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Erro ao buscar configurações adicionais", error.toException());
            }
        });

        btnAddFontSize.setOnClickListener(v -> {
            configuracoesRef.child("tamanho da fonte").setValue(++tamAtual);
        });

        btnRemFontSize.setOnClickListener(v -> {
            configuracoesRef.child("tamanho da fonte").setValue(--tamAtual);
        });
        btnSalvar.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("username", username);
            finishAfterTransition();
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