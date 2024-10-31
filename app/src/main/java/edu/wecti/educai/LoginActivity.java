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

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtSenha;
    private ProgressBar loginProgressBar;
    private FirebaseAuth myAuth;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myAuth = FirebaseAuth.getInstance();

        loginProgressBar = findViewById(R.id.loginProgressBar);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(senha)) {
                loginProgressBar.setVisibility(View.VISIBLE);
                myAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    abrirTelaPrincipal();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
                                    loginProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Insira seu email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insira sua senha", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        finish();
        startActivity(intent);
    }
}