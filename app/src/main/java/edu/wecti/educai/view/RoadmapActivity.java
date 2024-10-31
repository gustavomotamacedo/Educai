package edu.wecti.educai.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.wecti.educai.R;

public class RoadmapActivity extends AppCompatActivity {

    private MockView mockClickableTest;
    private TextView txtNome;
    private String username;

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

        Intent in = getIntent();
        username = in.getStringExtra("username");

        txtNome = findViewById(R.id.txtNome);
        txtNome.setText(username);

        mockClickableTest = findViewById(R.id.mockView);

        mockClickableTest.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResumoActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}