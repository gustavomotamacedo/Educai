package edu.wecti.educai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNome, edtEmail, edtSenha;
    private Button btnRegistrar;
    private ProgressBar registerProgressBar;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myAuth = FirebaseAuth.getInstance();
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        registerProgressBar = findViewById(R.id.registerProgressBar);

        btnRegistrar.setOnClickListener(v -> {
            String nome = String.valueOf(edtNome.getText());
            String email = String.valueOf(edtEmail.getText());
            String senha = String.valueOf(edtSenha.getText());

            if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {
                registerProgressBar.setVisibility(View.VISIBLE);
                myAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    abrirTelaPrincipal();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(CadastroActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
                                    registerProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            } else if (TextUtils.isEmpty(nome)) {
                Toast.makeText(this, "Insira um nome", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Insira um email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insira uma senha", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(CadastroActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}